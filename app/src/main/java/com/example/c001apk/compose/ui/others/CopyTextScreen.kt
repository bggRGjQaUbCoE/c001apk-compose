package com.example.c001apk.compose.ui.others

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.c001apk.compose.constant.Constants.UTF8
import com.example.c001apk.compose.util.Utils.richToString
import com.example.c001apk.compose.util.decode
import com.example.c001apk.compose.util.getAllLinkAndText
import java.net.URLDecoder

/**
 * Created by bggRGjQaUbCoE on 2024/6/7
 */
@Composable
fun CopyTextScreen(text: String) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        SelectionContainer {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = text
                        .decode
                        .getAllLinkAndText
                        .replace("\n", "<br/>")
                        .richToString(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        lineHeight = 35.sp
                    ),
                    modifier = Modifier
                        .padding(paddingValues)
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 25.dp)
                )
            }
        }
    }

}