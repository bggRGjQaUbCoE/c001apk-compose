package com.example.c001apk.compose.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.c001apk.compose.util.ImageShowUtil.getImageLp
import com.example.c001apk.compose.view.NineGridImageView

/**
 * Created by bggRGjQaUbCoE on 2024/6/5
 */
@Composable
fun NineImageView(
    modifier: Modifier = Modifier,
    pic: String?,
    picArr: List<String>?,
    feedType: String?,
    isSingle: Boolean = false
) {

    val primaryContainer = MaterialTheme.colorScheme.primaryContainer.toArgb()
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer.toArgb()

    AndroidView(
        modifier = modifier,
        factory = {
            NineGridImageView(it).apply {
                this.colorPrimaryContainer = primaryContainer
                this.colorOnPrimaryContainer = onPrimaryContainer
                this.isSingle = isSingle
            }
        },
        update = { imageView ->
            if (!picArr.isNullOrEmpty()) {
                if (picArr.size == 1 || feedType in listOf("feedArticle", "trade")) {
                    val imageLp = getImageLp(pic ?: picArr[0])
                    imageView.imgWidth = imageLp.first
                    imageView.imgHeight = imageLp.second
                }
                imageView.apply {
                    val urlList: MutableList<String> = ArrayList()
                    if (feedType in listOf("feedArticle", "trade") && imgWidth > imgHeight)
                        if (!pic.isNullOrEmpty()) urlList.add("$pic.s.jpg")
                        else urlList.add("${picArr[0]}.s.jpg")
                    else
                        urlList.addAll(picArr.map { "$it.s.jpg" })
                    setUrlList(urlList)
                }
            }
        }
    )
}