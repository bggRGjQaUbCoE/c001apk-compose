package com.example.c001apk.compose.ui.home.app

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.c001Application
import com.example.c001apk.compose.logic.model.AppItem
import com.example.c001apk.compose.logic.model.UpdateCheckItem
import com.example.c001apk.compose.util.Utils
import com.example.c001apk.compose.util.longVersionCodeCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
class AppListViewModel : ViewModel() {

    var appList by mutableStateOf<List<AppItem>>(emptyList())
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(true)
        private set

    init {
        fetchAppList()
    }

    val dataList = ArrayList<UpdateCheckItem>()

    private fun fetchAppList() {
        viewModelScope.launch(Dispatchers.IO) {
            val pm = c001Application.packageManager
            val infoList = pm.getInstalledApplications(PackageManager.GET_SHARED_LIBRARY_FILES)
            val itemList: MutableList<AppItem> = ArrayList()

            infoList.forEach { info ->
                if (((info.flags and ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM)) {
                    val packageInfo = pm.getPackageInfo(info.packageName, 0)

                    if (info.packageName != "com.example.c001apk.compose") {
                        val appItem = AppItem(
                            label = info.loadLabel(pm).toString(),
                            packageInfo = packageInfo
                        )
                        itemList.add(appItem)

                        dataList.add(
                            UpdateCheckItem(
                                info.packageName,
                                "0,${packageInfo.longVersionCodeCompat},${
                                    Utils.getInstalledAppMd5(
                                        info
                                    )
                                }"
                            )
                        )
                    }

                }
            }

            appList = itemList.sortedByDescending { it.packageInfo.lastUpdateTime }

            isRefreshing = false
            isLoading = false
        }
    }

    fun refresh() {
        if (!isRefreshing) {
            isRefreshing = true
            fetchAppList()
        }
    }

}