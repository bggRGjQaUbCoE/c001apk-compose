package com.example.c001apk.compose.util

import com.example.c001apk.compose.R

/**
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
object EmojiUtils {

    val emojiMap: LinkedHashMap<String, Int> = LinkedHashMap()

    val dataList by lazy { emojiMap.toList() }
    val emojiList = ArrayList<List<Pair<String, Int>>>()
    val coolBList = ArrayList<List<Pair<String, Int>>>()

    init {
        emojiMap["[置顶]"] = R.drawable.ic_feed_top
        emojiMap["[楼主]"] = R.drawable.ic_author
        emojiMap["[层主]"] = R.drawable.ic_subauthor
        emojiMap["[图片]"] = R.drawable.ic_photo
        emojiMap["[哈哈哈]"] = R.drawable.coolapk_emotion_1_hahaha
        emojiMap["[惊讶]"] = R.drawable.coolapk_emotion_2_jingya
        emojiMap["[呲牙]"] = R.drawable.coolapk_emotion_3_ciya
        emojiMap["[流泪]"] = R.drawable.coolapk_emotion_4_liulei
        emojiMap["[可爱]"] = R.drawable.coolapk_emotion_5_keai
        emojiMap["[微笑]"] = R.drawable.coolapk_emotion_6_weixiao
        emojiMap["[呵呵]"] = R.drawable.coolapk_emotion_7_hehe
        emojiMap["[撇嘴]"] = R.drawable.coolapk_emotion_8_piezui
        emojiMap["[色]"] = R.drawable.coolapk_emotion_9_se
        emojiMap["[傲慢]"] = R.drawable.coolapk_emotion_10_aoman
        emojiMap["[疑问]"] = R.drawable.coolapk_emotion_11_yiwen
        emojiMap["[无语]"] = R.drawable.coolapk_emotion_12_wuyu
        emojiMap["[坏笑]"] = R.drawable.coolapk_emotion_13_huaixiao
        emojiMap["[鄙视]"] = R.drawable.coolapk_emotion_14_bishi
        emojiMap["[发怒]"] = R.drawable.coolapk_emotion_15_fanu
        emojiMap["[爆怒]"] = R.drawable.coolapk_emotion_104
        emojiMap["[托腮]"] = R.drawable.coolapk_emotion_16_tuosai
        emojiMap["[吐舌]"] = R.drawable.coolapk_emotion_17_tushe
        emojiMap["[汗]"] = R.drawable.coolapk_emotion_18_han
        emojiMap["[抠鼻]"] = R.drawable.coolapk_emotion_19_koubi
        emojiMap["[亲亲]"] = R.drawable.coolapk_emotion_20_qinqin
        emojiMap["[喷血]"] = R.drawable.coolapk_emotion_21_penxue
        emojiMap["[笑眼]"] = R.drawable.coolapk_emotion_22_xiaoyan
        emojiMap["[睡]"] = R.drawable.coolapk_emotion_23_shui
        emojiMap["[捂嘴笑]"] = R.drawable.coolapk_emotion_24_wuzuixiao
        emojiMap["[再见]"] = R.drawable.coolapk_emotion_25_zaijian
        emojiMap["[可怜]"] = R.drawable.coolapk_emotion_26_kelian
        emojiMap["[笑哭]"] = R.drawable.coolapk_emotion_31_xiaoku
        emojiMap["[强]"] = R.drawable.coolapk_emotion_27_qiang
        emojiMap["[弱]"] = R.drawable.coolapk_emotion_28_ruo
        emojiMap["[抱拳]"] = R.drawable.coolapk_emotion_29_baoquan
        emojiMap["[ok]"] = R.drawable.coolapk_emotion_30_ok
        emojiMap["[嘿哈]"] = R.drawable.coolapk_emotion_32_heiha
        emojiMap["[捂脸]"] = R.drawable.coolapk_emotion_33_wulian
        emojiMap["[机智]"] = R.drawable.coolapk_emotion_34_jizhi
        emojiMap["[耶]"] = R.drawable.coolapk_emotion_35_ye
        emojiMap["[我最美]"] = R.drawable.coolapk_emotion_38_wozuimei
        emojiMap["[酷]"] = R.drawable.coolapk_emotion_36_ku
        emojiMap["[黑线]"] = R.drawable.coolapk_emotion_43_heixian
        emojiMap["[喷]"] = R.drawable.coolapk_emotion_44_pen
        emojiMap["[阴险]"] = R.drawable.coolapk_emotion_45_yinxian
        emojiMap["[难过]"] = R.drawable.coolapk_emotion_46_nanguo
        emojiMap["[委屈]"] = R.drawable.coolapk_emotion_47_weiqu
        emojiMap["[吃瓜]"] = R.drawable.coolapk_emotion_51_chigua
        emojiMap["[喝酒]"] = R.drawable.coolapk_emotion_52_hejiu
        emojiMap["[噗]"] = R.drawable.coolapk_emotion_53_pu
        emojiMap["[微微一笑]"] = R.drawable.coolapk_emotion_48_weiweiyixiao
        emojiMap["[欢呼]"] = R.drawable.coolapk_emotion_49_huanhu
        emojiMap["[白眼]"] = R.drawable.coolapk_emotion_84_baiyan
        emojiMap["[耐克嘴]"] = R.drawable.coolapk_emotion_81_naikezui
        emojiMap["[t耐克嘴]"] = R.drawable.coolapk_emotion_105
        emojiMap["[害羞]"] = R.drawable.coolapk_emotion_97_haixiu
        emojiMap["[无奈]"] = R.drawable.coolapk_emotion_98_wunai
        emojiMap["[皱眉]"] = R.drawable.coolapk_emotion_99_zhoumei
        emojiMap["[qqdoge]"] = R.drawable.coolapk_emotion_100_qqdoge
        emojiMap["[发呆]"] = R.drawable.coolapk_emotion_102_fadai
        emojiMap["[舒服]"] = R.drawable.coolapk_emotion_106
        emojiMap["[懒得理]"] = R.drawable.coolapk_emotion_107
        emojiMap["[不开心]"] = R.drawable.coolapk_emotion_108
        emojiMap["[挑眉坏笑]"] = R.drawable.coolapk_emotion_109
        emojiMap["[害怕]"] = R.drawable.coolapk_emotion_1010
        emojiMap["[哼唧]"] = R.drawable.coolapk_emotion_1011
        emojiMap["[挨打]"] = R.drawable.coolapk_emotion_1012
        emojiMap["[假笑]"] = R.drawable.coolapk_emotion_1014
        emojiMap["[偷看]"] = R.drawable.coolapk_emotion_1015
        emojiMap["[喝茶]"] = R.drawable.coolapk_emotion_1016
        emojiMap["[哦吼吼]"] = R.drawable.coolapk_emotion_1017
        emojiMap["[掩面笑]"] = R.drawable.coolapk_emotion_1018
        emojiMap["[表面哭泣]"] = R.drawable.coolapk_emotion_1019
        emojiMap["[表面开心]"] = R.drawable.coolapk_emotion_1020
        emojiMap["[滑稽]"] = R.drawable.coolapk_emotion_62_huaji
        emojiMap["[流汗滑稽]"] = R.drawable.coolapk_emotion_63_liuhanhuaji
        emojiMap["[受虐滑稽]"] = R.drawable.coolapk_emotion_64_shounuehuaji
        emojiMap["[cos滑稽]"] = R.drawable.coolapk_emotion_65_coshuaji
        emojiMap["[斗鸡眼滑稽]"] = R.drawable.coolapk_emotion_66_doujiyanhuaji
        emojiMap["[墨镜滑稽]"] = R.drawable.coolapk_emotion_67_mojinghuaji
        emojiMap["[小嘴滑稽]"] = R.drawable.coolapk_emotion_1013
        emojiMap["[针不戳]"] = R.drawable.coolapk_emotion_1022_zhenbuchuo
        emojiMap["[列文虎克]"] = R.drawable.coolapk_emotion_1023_liewenhuke
        emojiMap["[真正的音乐]"] = R.drawable.coolapk_emotion_1024
        emojiMap["[感知不强]"] = R.drawable.coolapk_emotion_1025
        emojiMap["[给我整一个]"] = R.drawable.coolapk_emotion_1026
        emojiMap["[yyds]"] = R.drawable.coolapk_emotion_1027
        emojiMap["[doge]"] = R.drawable.coolapk_emotion_37_doge
        emojiMap["[doge笑哭]"] = R.drawable.coolapk_emotion_56_dogexiaoku
        emojiMap["[doge呵斥]"] = R.drawable.coolapk_emotion_57_dogehechi
        emojiMap["[doge原谅ta]"] = R.drawable.coolapk_emotion_58_dogeyuanliangta
        emojiMap["[喵喵]"] = R.drawable.coolapk_emotion_82_miaomiao
        emojiMap["[二哈]"] = R.drawable.coolapk_emotion_59_erha
        emojiMap["[二哈盯]"] = R.drawable.coolapk_emotion_95_erhading
        emojiMap["[爱心]"] = R.drawable.coolapk_emotion_40_aixin
        emojiMap["[心碎]"] = R.drawable.coolapk_emotion_50_xinsui
        emojiMap["[玫瑰]"] = R.drawable.coolapk_emotion_41_meigui
        emojiMap["[凋谢]"] = R.drawable.coolapk_emotion_42_diaoxie
        emojiMap["[菜刀]"] = R.drawable.coolapk_emotion_39_caidao
        emojiMap["[牛啤]"] = R.drawable.coolapk_emotion_103_nb
        emojiMap["[py交易]"] = R.drawable.coolapk_emotion_101_pyjiaoyi
        emojiMap["[绿药丸]"] = R.drawable.coolapk_emotion_55_lvyaowan
        emojiMap["[红药丸]"] = R.drawable.coolapk_emotion_54_hongyaowan
        emojiMap["[酷安]"] = R.drawable.coolapk_emotion_60_kuan
        emojiMap["[酷安钓鱼]"] = R.drawable.coolapk_emotion_1021
        emojiMap["[绿帽]"] = R.drawable.coolapk_emotion_61_lvmao
        emojiMap["[酷安绿帽]"] = R.drawable.coolapk_emotion_96_kuanlvmao
        emojiMap["[火把]"] = R.drawable.coolapk_emotion_83_huoba
        emojiMap["[夏阁艾迪剑]"] = R.drawable.coolapk_emotion_1028
        emojiMap["[下次一定]"] = R.drawable.coolapk_emotion_1029
        emojiMap["[酷安土豆]"] = R.drawable.coolapk_emotion_1030
        emojiMap["[头条通知书]"] = R.drawable.coolapk_emotion_1031
        emojiMap["[酷币]"] = R.drawable.c_coolb
        emojiMap["[酷币1分]"] = R.drawable.c_onef
        emojiMap["[酷币2分]"] = R.drawable.c_twof
        emojiMap["[酷币5分]"] = R.drawable.c_fivef
        emojiMap["[酷币1毛]"] = R.drawable.c_onem
        emojiMap["[酷币2毛]"] = R.drawable.c_twom
        emojiMap["[酷币5毛]"] = R.drawable.c_fivem
        emojiMap["[酷币1块]"] = R.drawable.c_oney
        emojiMap["[酷币2块]"] = R.drawable.c_twoy
        emojiMap["[酷币5块]"] = R.drawable.c_fivey
        emojiMap["[酷币10块]"] = R.drawable.c_teny
        emojiMap["[酷币20块]"] = R.drawable.c_ty
        emojiMap["[酷币50块]"] = R.drawable.c_fy
        emojiMap["[酷币100块]"] = R.drawable.c_oy
        emojiMap["[酷币1$]"] = R.drawable.c_oned
        emojiMap["[酷币2$]"] = R.drawable.c_twod
        emojiMap["[酷币5$]"] = R.drawable.c_fived
        emojiMap["[酷币1€]"] = R.drawable.c_oneo
        emojiMap["[酷币2€]"] = R.drawable.c_twoo
        emojiMap["[酷币5€]"] = R.drawable.c_fiveo
        emojiMap["[灰色酷币]"] = R.drawable.coolapk_emotion_68
        emojiMap["[绿色酷币]"] = R.drawable.coolapk_emotion_69
        emojiMap["[白纹酷币]"] = R.drawable.coolapk_emotion_70
        emojiMap["[新酷币]"] = R.drawable.coolapk_emotion_71
        emojiMap["[新币1分]"] = R.drawable.coolapk_emotion_72
        emojiMap["[新酷币2分]"] = R.drawable.coolapk_emotion_85
        emojiMap["[新酷币5分]"] = R.drawable.coolapk_emotion_86
        emojiMap["[新酷币1毛]"] = R.drawable.coolapk_emotion_87
        emojiMap["[新酷币2毛]"] = R.drawable.coolapk_emotion_88
        emojiMap["[新酷币5毛]"] = R.drawable.coolapk_emotion_89
        emojiMap["[新酷币1块]"] = R.drawable.coolapk_emotion_90
        emojiMap["[新酷币2块]"] = R.drawable.coolapk_emotion_91
        emojiMap["[新酷币5块]"] = R.drawable.coolapk_emotion_92
        emojiMap["[新酷币10块]"] = R.drawable.coolapk_emotion_93
        emojiMap["[新酷币20块]"] = R.drawable.coolapk_emotion_94
        emojiMap["[新酷币50块]"] = R.drawable.coolapk_emotion_73
        emojiMap["[新酷币100块]"] = R.drawable.coolapk_emotion_74
        emojiMap["[新酷币1$]"] = R.drawable.coolapk_emotion_75
        emojiMap["[新酷币2$]"] = R.drawable.coolapk_emotion_76
        emojiMap["[新酷币5$]"] = R.drawable.coolapk_emotion_77
        emojiMap["[新酷币1€]"] = R.drawable.coolapk_emotion_78
        emojiMap["[新酷币2€]"] = R.drawable.coolapk_emotion_79
        emojiMap["[新酷币5€]"] = R.drawable.coolapk_emotion_80

        for (i in 0..3) {
            emojiList.add(dataList.subList(i * 27 + 4, (i + 1) * 27 + 4))
        }
        coolBList.add(dataList.subList(112, 139))
        coolBList.add(dataList.subList(139, 155))
    }

}
