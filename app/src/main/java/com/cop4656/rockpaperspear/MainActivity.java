package com.cop4656.rockpaperspear;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.view.View.OnClickListener;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    //The score integers
    private int playerScore;
    private int computerScore;

    private boolean playerWin;
    private boolean computerWin;

    private boolean gameFinished;

    private RadioGroup optionGroup;
    private RadioButton rockOption;
    private RadioButton paperOption;
    private RadioButton spearOption;
    private TextView statusIndicator;
    private Button submitChoiceButton;
    private Button resetButton;
    private TextView playerScoreText;
    private TextView computerScoreText;

    MediaPlayer winMediaPlayer;
    MediaPlayer loseMediaPlayer;
    MediaPlayer drawMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        winMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.win);
        loseMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.fail);
        drawMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.draw);

        //get references to all widgets needing modifications
        optionGroup = (RadioGroup) findViewById(R.id.optionGroup);
        rockOption = (RadioButton) findViewById(R.id.rockOption);
        paperOption = (RadioButton) findViewById(R.id.paperOption);
        spearOption = (RadioButton) findViewById(R.id.spearOption);
        statusIndicator = (TextView) findViewById(R.id.statusIndicator);
        playerScoreText = (TextView) findViewById(R.id.playerScore);
        computerScoreText = (TextView) findViewById(R.id.computerScore);

        submitChoiceButton = (Button) findViewById(R.id.submitChoiceButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        submitChoiceButton.setOnClickListener(submitButtonListener);
        resetButton.setOnClickListener(resetButtonListener);

        playerWin = false;
        computerWin = false;
        gameFinished = false;
    }

    private final OnClickListener submitButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Button submitButton = ((Button) v);

            winMediaPlayer.setLooping(false);
            loseMediaPlayer.setLooping(false);
            drawMediaPlayer.setLooping(false);

            if (!gameFinished) {
                //obtain the option in the radio group
                int selectedId = optionGroup.getCheckedRadioButtonId();

                RadioButton checkedOption = (RadioButton) findViewById(selectedId);

                RadioButton computerOption;

                //attempt to determine which option was selected
                try {
                    if (selectedId == -1)
                        throw new Exception();
                    //generate computer choice
                    SecureRandom random = new SecureRandom();
                    int compChoice = random.nextInt(3); //will choose 0-2

                    switch (compChoice) {
                        case 0:
                            computerOption = rockOption;
                            break;
                        case 1:
                            computerOption = paperOption;
                            break;
                        default:
                            computerOption = spearOption;
                            break;
                    }

                    //Determine player win
                    if (checkedOption == rockOption) {
                        if (computerOption == rockOption) {
                            playerWin = false;
                            computerWin = false;
                        } else if (computerOption == paperOption) {
                            playerWin = false;
                            computerWin = true;
                        } else {
                            playerWin = true;
                            computerWin = false;
                        }
                    } else if (checkedOption == paperOption) {
                        if (computerOption == rockOption) {
                            playerWin = true;
                            computerWin = false;
                        } else if (computerOption == paperOption) {
                            playerWin = false;
                            computerWin = false;
                        } else {
                            playerWin = false;
                            computerWin = true;
                        }
                    } else //if checkedOption == spearOption
                    {
                        if (computerOption == rockOption) {
                            playerWin = false;
                            computerWin = true;
                        } else if (computerOption == paperOption) {
                            playerWin = true;
                            computerWin = false;
                        } else { //computer chose spear
                            playerWin = false;
                            computerWin = false;
                        }
                    }
                } catch (Exception e) {  //will execute if there is an exception
                    statusIndicator.setText(R.string.select_choice_error);
                    return;
                }

                //now, set appropriate text.
                if (playerWin) {
                    CharSequence resultText = getText(R.string.player_win_indicate_text);
                    CharSequence computerChoiceText = getText(R.string.computer_choice);
                    CharSequence computerChoiceItem = computerOption.getText();

                    CharSequence displayText = resultText + " " + computerChoiceText + " " + computerChoiceItem;

                    winMediaPlayer.start();
                    statusIndicator.setText(displayText);
                    ++playerScore;

                } else if (computerWin) {
                    CharSequence resultText = getText(R.string.player_lose_indicate_text);
                    CharSequence computerChoiceText = getText(R.string.computer_choice);
                    CharSequence computerChoiceItem = computerOption.getText();

                    CharSequence displayText = resultText + " " + computerChoiceText + " " + computerChoiceItem;
                    loseMediaPlayer.start();
                    statusIndicator.setText(displayText);
                    ++computerScore;
                } else {
                    CharSequence resultText = getText(R.string.game_draw);
                    CharSequence computerChoiceText = getText(R.string.computer_choice);
                    CharSequence computerChoiceItem = computerOption.getText();

                    CharSequence displayText = resultText + " " + computerChoiceText + " " + computerChoiceItem;
                    drawMediaPlayer.start();
                    statusIndicator.setText(displayText);
                }

                playerScoreText.setText(Integer.toString(playerScore));
                computerScoreText.setText(Integer.toString(computerScore));
                gameFinished = true;
                submitButton.setText(R.string.play_again_choice);

                for (int i = 0; i < optionGroup.getChildCount(); i++) {
                    optionGroup.getChildAt(i).setEnabled(false);
                }
            }
            else //the game was just finished, gameFinished = true
            {




                submitButton.setText(R.string.button_text_choose);
                optionGroup.clearCheck();
                statusIndicator.setText(R.string.inital_status);
                gameFinished = false;

                for (int i = 0; i < optionGroup.getChildCount(); i++) {
                    optionGroup.getChildAt(i).setEnabled(true);
                }
            }
        }
    };

    private final OnClickListener resetButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Button resetButton = ((Button) v);
            playerScore = 0;
            computerScore = 0;
            playerScoreText.setText(Integer.toString(playerScore));
            computerScoreText.setText(Integer.toString(computerScore));
            statusIndicator.setText(R.string.inital_status);
            submitChoiceButton.setText(R.string.button_text_choose);
            gameFinished = false;
            optionGroup.clearCheck();
            for (int i = 0; i < optionGroup.getChildCount(); i++) {
                optionGroup.getChildAt(i).setEnabled(true);
            }
        }
    };


}
