package blackjack.domain;

import blackjack.domain.card.Card;
import blackjack.domain.card.Deck;
import blackjack.domain.money.Profits;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Participant;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import blackjack.domain.result.MatchResult;
import blackjack.domain.result.ProfitResult;

import java.math.BigDecimal;
import java.util.*;

public class BlackJackGame {
    private static final BigDecimal LOSE_RATE = new BigDecimal("-1");

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

    public ProfitResult calculateProfit(Map<Player, MatchResult> result) {
        Map<Participant, Profits> profitResult = new LinkedHashMap<>();
        Profits dealerProfit = new Profits(BigDecimal.ZERO);
        profitResult.put(dealer, dealerProfit);

        for (Player player : result.keySet()) {
            Profits profit = finalProfitByEachStatus(result.get(player), player.profit());
            profitResult.put(player, profit);
            dealerProfit = dealerProfit.accumulate(profit.multiply(LOSE_RATE));
        }

        profitResult.put(dealer, dealerProfit);
        return new ProfitResult(profitResult);
    }

    private Profits finalProfitByEachStatus(MatchResult matchResult, Profits profit) {
        return matchResult.finalProfitByEachStatus(profit);
    }

    public Players getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
