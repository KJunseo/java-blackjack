package blackjack.controller;

import blackjack.domain.BlackJackGame;
import blackjack.domain.money.BettingMoney;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Nickname;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import blackjack.dto.ParticipantDto;
import blackjack.view.InputView;
import blackjack.view.OutputView;

import java.util.List;
import java.util.stream.Collectors;

public class BlackJackController {

    public void start() {
        try {
            BlackJackGame blackJackGame = new BlackJackGame(players());
            Players players = blackJackGame.getPlayers();
            bettingPlayers(players);
            distributeCards(blackJackGame);
            playersTurn(players, blackJackGame);
            dealerTurn(blackJackGame.getDealer(), blackJackGame);
            showProfitResult(blackJackGame);
        } catch (RuntimeException e) {
            OutputView.printError(e.getMessage());
            start();
        }
    }

    private Players players() {
        try {
            OutputView.enterPlayersName();
            return new Players(InputView.inputName().stream()
                    .map(Nickname::new)
                    .map(Player::new)
                    .collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
            return players();
        }
    }

    private void bettingPlayers(Players players) {
        players.getPlayers()
                .forEach(this::bettingEachPlayer);
    }

    public void bettingEachPlayer(Player player) {
        try {
            OutputView.askBettingMoney(player);
            player.betting(new BettingMoney(InputView.inputBettingMoney()));
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
            bettingEachPlayer(player);
        }
    }

    private void distributeCards(BlackJackGame blackJackGame) {
        blackJackGame.distributeCards();
        OutputView.distributeFirstTwoCard(playersDto(blackJackGame.getPlayers()), new ParticipantDto(blackJackGame.getDealer()));
    }

    private void playersTurn(Players players, BlackJackGame blackJackGame) {
        for (Player player : players.getPlayers()) {
            eachPlayerTurn(player, blackJackGame);
        }
    }

    private void eachPlayerTurn(Player player, BlackJackGame blackJackGame) {
        while (player.canDraw() && askDrawCard(player)) {
            player.draw(blackJackGame.drawOneCard());
            OutputView.showCards(new ParticipantDto(player));
        }
    }

    private boolean askDrawCard(Player player) {
        try {
            OutputView.askOneMoreCard(new ParticipantDto(player));
            boolean wantDraw = InputView.inputAnswer();
            playerWantStopDraw(player, wantDraw);
            return wantDraw;
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
            return askDrawCard(player);
        }
    }

    private void playerWantStopDraw(Player player, boolean wantDraw) {
        if (!wantDraw) {
            OutputView.showCards(new ParticipantDto(player));
            player.stay();
        }
    }

    private void dealerTurn(Dealer dealer, BlackJackGame blackJackGame) {
        while (dealer.canDraw()) {
            dealer.draw(blackJackGame.drawOneCard());
            OutputView.dealerReceiveOneCard();
        }
        if (dealer.isHit()) {
            dealer.stay();
        }
    }

    private void showProfitResult(BlackJackGame blackJackGame) {
        Players players = blackJackGame.getPlayers();
        Dealer dealer = blackJackGame.getDealer();
        OutputView.showAllCards(playersDto(players), new ParticipantDto(dealer));
        OutputView.showFinalProfitResult(blackJackGame.calculateProfit());
    }

    public List<ParticipantDto> playersDto(Players players) {
        return players.getPlayers().stream()
                .map(ParticipantDto::new)
                .collect(Collectors.toList());
    }
}
