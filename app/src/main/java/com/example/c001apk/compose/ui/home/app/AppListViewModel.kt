package com.example.c001apk.compose.ui.home.app

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.c001Application
import com.example.c001apk.compose.logic.model.AppItem
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.util.PrefManager
import com.example.c001apk.compose.util.Utils
import com.example.c001apk.compose.util.Utils.getBase64
import com.example.c001apk.compose.util.longVersionCodeCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@HiltViewModel
class AppListViewModel @Inject constructor(
    private val networkRepo: NetworkRepo
) : ViewModel() {

    init {
        fetchAppList()
    }

    var appList by mutableStateOf<List<AppItem>>(emptyList())
        private set
    var isRefreshing by mutableStateOf(false)
        private set

    private fun fetchAppList() {
        viewModelScope.launch(Dispatchers.IO) {
            val pm = c001Application.packageManager
            val infoList = pm.getInstalledApplications(PackageManager.GET_SHARED_LIBRARY_FILES)
            val itemList: MutableList<AppItem> = ArrayList()
            val updateCheckJsonObject = JSONObject()

            infoList.forEach { info ->
                if (((info.flags and ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM)) {
                    val packageInfo = pm.getPackageInfo(info.packageName, 0)

                    if (info.packageName != "com.example.c001apk.compose") {
                        val appItem = AppItem(
                            label = info.loadLabel(pm).toString(),
                            packageInfo = packageInfo
                        )
                        itemList.add(appItem)
                        updateCheckJsonObject.put(
                            info.packageName,
                            "0,${packageInfo.longVersionCodeCompat},${Utils.getInstalledAppMd5(info)}"
                        )
                    }

                }
            }

            appList = itemList.sortedByDescending { it.packageInfo.lastUpdateTime }
            if (PrefManager.isCheckUpdate)
                fetchAppsUpdate(updateCheckJsonObject.toString().getBase64(false))
            isRefreshing = false
        }
    }

    private fun fetchAppsUpdate(pkg: String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getAppsUpdate(pkg)
                .collect { result ->
                    result.getOrNull()?.let {

                    }
                }
        }
    }

    fun refresh() {
        if (!isRefreshing) {
            isRefreshing = true
            fetchAppList()
        }
    }

}