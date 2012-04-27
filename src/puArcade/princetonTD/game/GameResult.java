package puArcade.princetonTD.game;

import java.util.ArrayList;

import puArcade.princetonTD.players.Team;


public class GameResult {

	private Team winner;
    private ArrayList<Team> teams;

    public GameResult(Team winner)
    {
        this.winner = winner;
    }

    public void setTeamGagnante(Team winner)
    {
        this.winner = winner;
    }

    public Team getTeamGagnante()
    {
        return winner;
    }

    public ArrayList<Team> getTeams()
    {
        return teams;
    }

}
