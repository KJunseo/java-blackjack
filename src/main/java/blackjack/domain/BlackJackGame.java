package blackjack.domain;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Players;
import blackjack.domain.result.ProfitResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackJackGame {
    public static final BigDecimal LOSE_RATE = new BigDecimal("-1");

    private final Players players;
    private final Dealer dealer;
    private final Deck deck;

    public BlackJackGame(Players players) {
        this.players = players;
        this.dealer = new Dealer();
        List<Card> cards = new ArrayList<>(Card.values());
        Collections.shuffle(cards);
        this.deck = new Deck(cards);
    }

    public void distributeCards() {
        dealer.firstDraw(deck.drawCard(), deck.drawCard());
        players.eachPlayerFirstDraw(deck);
    }

    public Card drawOneCard() {
        return deck.drawCard();
    }

    public ProfitResult calculateProfit() {
        return players.calculateFinalProfit(dealer);
    }

    public Players getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
