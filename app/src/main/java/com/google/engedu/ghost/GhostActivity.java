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

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false ;
    private Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUserTurn(){
        userTurn = random.nextBoolean();
    }

    public boolean UserTurn(){
        return userTurn;
    }
    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        setUserTurn();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView gameStatus = (TextView) findViewById(R.id.gameStatus);
        TextView wordFragment = (TextView) findViewById(R.id.ghostText);
        String word = wordFragment.getText().toString();
        if (dictionary.isWord(word)&&word.length()>=4) {
            gameStatus.setText("COMPUTER WINS!");
            return;
        }
        if(dictionary.getAnyWordStartingWith(word)==null){
            gameStatus.setText("you can't bluff this computer!");
            return;
            }
        if(dictionary.getAnyWordStartingWith(word)!=null){
            word+=dictionary.getAnyWordStartingWith(word).charAt(word.length());
            wordFragment.setText(word);
            }


        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        gameStatus.setText(USER_TURN);
    }


    /**
     * Handler for the "Challenge" button.
     * Challenge the other player's word if the player think there is a valid word of at least 4 letters
     * @param view
     * @return true
     */
    public void challenge(View view) {
        TextView gameStatus = (TextView) findViewById(R.id.gameStatus);
        TextView wordFragment = (TextView) findViewById(R.id.ghostText);
        String word = wordFragment.getText().toString();
        if (dictionary.isWord(word) && word.length() >= 4) {
            gameStatus.setText("Congratulations! You won!");
            return;
        }
        if (dictionary.getAnyWordStartingWith(word) == null) {
            gameStatus.setText("you can't bluff me");
            return;
        }
        if (dictionary.getAnyWordStartingWith(word) != null) {
            gameStatus.setText("Computer Wins");
            wordFragment.setText(dictionary.getAnyWordStartingWith(word));
            return;
        }
        //disable keyboard input
    }

    private void enableChallengeButton(boolean bool){
        findViewById(R.id.challenge).setEnabled(bool);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        TextView gameStatus = (TextView) findViewById(R.id.gameStatus);
        TextView wordFragment = (TextView) findViewById(R.id.ghostText);
        char letter;
        String word;
        if (Character.isLetter(event.getUnicodeChar())){
            letter = (char) event.getUnicodeChar();
             word=wordFragment.getText().toString()+letter;
             wordFragment.setText(word);
             gameStatus.setText(COMPUTER_TURN);
             computerTurn();
        }


        return super.onKeyUp(keyCode, event);

    }
}
