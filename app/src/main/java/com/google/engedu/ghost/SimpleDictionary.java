/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Random rand = new Random();
        int pick = rand.nextInt(words.size());
        if (prefix == "") {
            return words.get(pick);
        }
        int start = 0, end = words.size() - 1;
        while (start <= end) {
            int m = (start + end) / 2;
            if (words.get(m).startsWith(prefix))
                return words.get(m);
            else if (prefix.compareTo(words.get(m)) > 0)
                start = m + 1;
            else
                end = m - 1;
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix, boolean userTurn) {
        Random rand = new Random();
        int pick = rand.nextInt(words.size());
        if (prefix == "") {
            return words.get(pick);
        }
        int start = 0, end = words.size() - 1;
        String selected = null;
        ArrayList<String> odd = new ArrayList<String>();
        ArrayList<String> even = new ArrayList<String>();
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).length() % 2 == 0) {
                even.add(words.get(i));
            } else {
                odd.add(words.get(i));
            }
        }
        if (userTurn)
            while (start <= end) {
                int m = (start + end) / 2;
                if (even.get(m).startsWith(prefix))
                    selected = even.get(m);
                else if (prefix.compareTo(even.get(m)) > 0)
                    start = m + 1;
                else
                    end = m - 1;
            }
        else
            while (start <= end) {
                int m = (start + end) / 2;
                if (odd.get(m).startsWith(prefix))
                    selected = odd.get(m);
                else if (prefix.compareTo(odd.get(m)) > 0)
                    start = m + 1;
                else
                    end = m - 1;
                return selected;
                }

        return selected;
    }
}
