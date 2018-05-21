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
        "34", "40730",
        "35", "55",
        "36", "872187",
        "39", "840",
        "40", "210",
        "41", "7652413",
        "44", "5482660",
        "64", "1322",
        "65", "272",
        "66", "661",
        "76", "190569291",
        "78", "55374",
        "94", "518408346",
        "95", "14316",
        "100", "756872327473",
        "118", "44680",
        "122", "1582",
        "126", "18522",
        "127", "18407904",
        "135", "4989",
        "138", "1118049290473932",
        "139", "10057761",
        "141", "878454337159",
        "142", "1006193",
        "148", "2129970655314432",
        "149", "52852124",
        "150", "-271248680",
        "151", "0.464399",
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
        "179", "986262",
        "183", "48861552",
        "188", "95962097",
        "193", "684465067343069",
        "197", "1.710637717",
        "204", "2944730",
        "207", "44043947822",
        "211", "1922364685",
        "214", "1677366278943",
        "216", "5437849",
        "218", "15915492",
        "231", "7526965179680",
        "234", "1259187438574927161",
        "239", "0.001887854841",
        "240", "7448717393364181966",
        "248", "23507044290",
        "249", "9275262564250418",
        "250", "1425480602091519",
        "258", "12747994",
        "260", "167542057",
        "263", "2039506520",
        "268", "785478606870985",
        "273", "2032447591196869022",
        "277", "1125977393124310",
        "282", "1098988351",
        "284", "5a411d7b",
        "288", "605857431263981935",
        "301", "2178309",
        "302", "1170060",
        "303", "1111981904675169",
        "304", "283988410192",
        "306", "852938",
        "310", "2586528661783",
        "315", "13625242",
        "325", "54672965",
        "346", "336108797689259276",
        "347", "11109800204052",
        "348", "1004195061",
        "351", "11762187201804552",
        "357", "1739023853137",
        "381", "139602943319822",
        "407", "39782849136421",
        "429", "98792821",
        "451", "153651073760956",
        "501", "197912312715",
        "504", "694687",
        "512", "50660591862310323",
        "516", "939087315",
        "518", "100315739184392",
        "531", "4515432351156203105",
        "549", "476001479068717",
        "571", "30510390701978",
        "607", "13.1265108586",
        "615", "108424772"
    };

    static Map<Integer, String> getSolutions() {
        Map<Integer, String> result = new HashMap<>();
        for (int i = 0; i < solutions.length; i += 2)
            result.put(Integer.parseInt(solutions[i]), solutions[i+1]);
        return result;
    }
}
