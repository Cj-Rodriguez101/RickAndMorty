package com.example.rickandmorty.util

object VideoUtils {

    private val videos: HashMap<String, String> = hashMapOf("S01E01" to "qtdCIs6JdXg",
        "S01E02" to "5qT144r5d3w", "S01E03" to "g00-7nGOI5o", "S01E04" to "qVpqaac4mZ0",
        "S01E05" to "U_f63SBFJig", "S01E06" to "_-2NIvUBpu8", "S01E07" to "8oi12dCzHG4",
        "S01E08" to "Beki0y3znwI", "S01E09" to "ONfWJ7zpERs", "S01E10" to "NIM9sEQSAgw",
        "S01E11" to "lYtQTF3wlLQ", "S02E01" to "MLwI8JUU7YM", "S02E02" to "Mt9qal9WTpw",
        "S02E03" to "KI8fVZhXaiU", "S02E04" to "3PakCvsgVpk", "S02E05" to "ZlFCAx4iXgc",
        "S02E06" to "wvZlW-b82Q0", "S02E07" to "Ye7FhTtNR2A", "S02E08" to "nxhBbJSNXWA",
        "S02E09" to "77jqLe7lA34","S02E10" to "atZ_wpKEPdU", "S03E01" to "RHWBS_xxI_o",
        "S03E02" to "6pAOZw-2EJ0", "S03E03" to "9d-ekfDjjpQ", "S03E04" to "zysb49M8Rns",
        "S03E05" to "XE9pbj1gd-Y", "S03E06" to "7OlbOzFIKsA", "S03E07" to "ei65xpQMbpA",
        "S03E08" to "kYbVHV055Do", "S03E09" to "ACSBy6GsrG8","S03E10" to "ykL-iMtpV50",
        "S04E01" to "2NSa0ABIH5Y", "S04E02" to "jWFxlRpcjd0", "S04E03" to "YYy6IoPIx7U",
        "S04E04" to "6gprED0rWRk", "S04E05" to "eZoDwcTKrYY", "S04E06" to "Jrp06y4mtl0",
        "S04E07" to "eDZAic0DH8Y", "S04E08" to "K8DaX0FFthc", "S04E09" to "ctBxpKer-QY",
        "S04E10" to "Ul4yZ5VzLNA","S05E01" to "9WGSdzhsxjU", "S05E02" to "-sr3hZCAono",
        "S05E03" to "-KzlmniLE0", "S05E04" to "vLCPW6MnTHo", "S05E05" to "sjvlW0AzKlY",
        "S05E06" to "LgXPLKffxK4", "S05E07" to "rgpOkDFU4HA", "S05E08" to "AvgqcKOXSUE",
        "S05E09" to "r1QJ08dsIKA", "S05E10" to "bl1ktxb4FvU")

    fun getVideoIdBasedOnEpisode(episode: String): String?{
        return videos[episode]
    }
}