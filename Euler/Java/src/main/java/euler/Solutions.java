package euler;

import java.util.HashMap;
import java.util.Map;

final class Solutions {
    private Solutions() {}

    private static final String[] solutions = {
        "1", "233168",
        "2", "4613732",
        "3", "6857",
        "4", "906609",
        "5", "232792560",
        "6", "25164150",
        "7", "104743",
        "8", "23514624000",
        "9", "31875000",
        "10", "142913828922",
        "12", "76576500",
        "14", "837799",
        "16", "1366",
        "17", "21124",
        "20", "648",
        "21", "31626",
        "23", "4179871",
        "24", "2783915460",
        "26", "983",
        "34", "40730",
        "35", "55",
        "36", "872187",
        "39", "840",
        "40", "210",
        "41", "7652413",
        "44", "5482660",
        "57", "153",
        "64", "1322",
        "65", "272",
        "66", "661",
        "71", "428570",
        "72", "303963552391",
        "73", "7295372",
        "76", "190569291",
        "78", "55374",
        "94", "518408346",
        "95", "14316",
        "100", "756872327473",
        "104", "329468",
        "107", "259679",
        "118", "44680",
        "122", "1582",
        "126", "18522",
        "127", "18407904",
        "128", "14516824220",
        "135", "4989",
        "138", "1118049290473932",
        "139", "10057761",
        "141", "878454337159",
        "142", "1006193",
        "148", "2129970655314432",
        "149", "52852124",
        "150", "-271248680",
        "151", "0.464399",
        "152", "301",
        "153", "17971254122360635",
        "154", "479742450",
        "155", "3857447",
        "158", "409511334375",
        "159", "14489159",
        "160", "16576",
        "165", "2868868",
        "166", "7130034",
        "169", "178653872807",
        "170", "9857164023",
        "171", "142989277",
        "172", "227485267000992000",
        "174", "209566",
        "175", "1,13717420,8",
        "178", "126461847755",
        "179", "986262",
        "182", "399788195976",
        "183", "48861552",
        "185", "4640261571849533",
        "186", "2325629",
        "188", "95962097",
        "190", "371048281",
        "191", "1918080160",
        "192", "57060635927998347",
        "193", "684465067343069",
        "194", "61190912",
        "196", "322303240771079935",
        "197", "1.710637717",
        "199", "0.00396087",
        "200", "229161792008",
        "204", "2944730",
        "207", "44043947822",
        "211", "1922364685",
        "214", "1677366278943",
        "216", "5437849",
        "218", "15915492",
        "222", "1590933",
        "223", "61614848",
        "224", "4137330",
        "225", "2009",
        "231", "7526965179680",
        "233", "271204031455541309",
        "234", "1259187438574927161",
        "239", "0.001887854841",
        "240", "7448717393364181966",
        "248", "23507044290",
        "249", "9275262564250418",
        "250", "1425480602091519",
        "258", "12747994",
        "260", "167542057",
        "261", "238890850232021",
        "263", "2039506520",
        "266", "1096883702440585",
        "268", "785478606870985",
        "273", "2032447591196869022",
        "277", "1125977393124310",
        "282", "1098988351",
        "284", "5a411d7b",
        "288", "605857431263981935",
        "293", "2209",
        "297", "2252639041804718029",
        "301", "2178309",
        "302", "1170060",
        "303", "1111981904675169",
        "304", "283988410192",
        "306", "852938",
        "310", "2586528661783",
        "315", "13625242",
        "323", "6.3551758451",
        "325", "54672965",
        "333", "3053105",
        "346", "336108797689259276",
        "347", "11109800204052",
        "348", "1004195061",
        "351", "11762187201804552",
        "357", "1739023853137",
        "358", "3284144505",
        "365", "162619462356610313",
        "370", "41791929448408",
        "374", "334420941",
        "375", "7435327983715286168",
        "399", "1508395636674243,6.5e27330467",
        "381", "139602943319822",
        "401", "281632621",
        "407", "39782849136421",
        "417", "446572970925740",
        "429", "98792821",
        "435", "252541322550",
        "451", "153651073760956",
        "455", "450186511399999",
        "473", "35856681704365",
        "479", "191541795",
        "485", "51281274340",
        "500", "35407281",
        "501", "197912312715",
        "504", "694687",
        "512", "50660591862310323",
        "516", "939087315",
        "518", "100315739184392",
        "531", "4515432351156203105",
        "549", "476001479068717",
        "571", "30510390701978",
        "577", "265695031399260211",
        "581", "2227616372734",
        "587", "2240",
        "588", "11651930052",
        "601", "1617243",
        "607", "13.1265108586",
        "615", "108424772",
        "625", "551614306",
    };

    static Map<Integer, String> getSolutions() {
        Map<Integer, String> result = new HashMap<>();
        for (int i = 0; i < solutions.length; i += 2)
            result.put(Integer.parseInt(solutions[i]), solutions[i+1]);
        return result;
    }
}
