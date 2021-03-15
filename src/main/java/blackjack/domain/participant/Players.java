package blackjack.domain.participant;

import blackjack.domain.card.Deck;
import blackjack.domain.money.Profits;
import blackjack.domain.result.MatchResult;
import blackjack.domain.result.ProfitResult;

import java.math.BigDecimal;
import java.util.*;

import static blackjack.domain.BlackJackGame.LOSE_RATE;

public class Players {
    private static final String PLAYER_NUMBER_ERROR_MESSAGE = "인원 수는 2 ~ 8명입니다.";
    private static final String PLAYER_DUPLICATE_ERROR_MESSAGE = "중복된 닉네임을 사용할 수 없습니다.";
    private static final int MIN_PLAYER_COUNT = 2;
    private static final int MAX_PLAYER_COUNT = 8;

    private final List<Player> players;

    public Players(List<Player> players) {
        validatePlayerCount(players);
        validateDuplicate(players);
        this.players = players;
    }

    private void validatePlayerCount(List<Player> player) {
        if (player.size() < MIN_PLAYER_COUNT || player.size() > MAX_PLAYER_COUNT) {
            throw new IllegalArgumentException(PLAYER_NUMBER_ERROR_MESSAGE);
        }
    }

    private void validateDuplicate(List<Player> players) {
        Set<Player> set = new HashSet<>(players);
        if (players.size() != set.size()) {
            throw new IllegalArgumentException(PLAYER_DUPLICATE_ERROR_MESSAGE);
        }
    }

    public void eachPlayerFirstDraw(Deck deck) {
        for (Player player : players) {
            player.firstDraw(deck.drawCard(), deck.drawCard());
        }
    }

    public ProfitResult calculateFinalProfit(Dealer dealer) {
        Map<Participant, Profits> profitResult = new LinkedHashMap<>();
        Profits dealerProfit = new Profits(BigDecimal.ZERO);
        profitResult.put(dealer, dealerProfit);

        for (Player player : players) {
            Profits profit = finalProfitByEachStatus(dealer.matchGame(player), player.profit());
            profitResult.put(player, profit);
            dealerProfit = dealerProfit.accumulate(profit.multiply(LOSE_RATE));
        }

        profitResult.put(dealer, dealerProfit);
        return new ProfitResult(profitResult);
    }

    private Profits finalProfitByEachStatus(MatchResult matchResult, Profits profit) {
        return matchResult.finalProfitByEachStatus(profit);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}
