package com.mygdx.game.Scores;

/**
 * Created by fanda on 19.06.2017.
 */

public class HighScore {

    private String playerName;
    private String score;
    private String gameMode;
    private String diffLvl;

    public HighScore(String score, String diffLvl, String gameMode, String playerName) {
        this.playerName = playerName;
        this.score = score;
        this.gameMode = gameMode;
        this.diffLvl = diffLvl;
    }


    public String getPlayerName() {
        return playerName;
    }

    public String getScore() {
        return score;
    }

    public String getGameMode() {
        return gameMode;
    }

    public String getDiffLvl() {
        return diffLvl;
    }
}
