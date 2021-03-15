package blackjack.domain.participant;

import blackjack.domain.card.Card;
import blackjack.domain.card.Cards;
import blackjack.domain.card.Denomination;
import blackjack.domain.card.Shape;
import blackjack.domain.money.BettingMoney;
import blackjack.domain.money.Profits;
import blackjack.domain.result.ProfitResult;
import blackjack.domain.state.BlackJack;
import blackjack.domain.state.Bust;
import blackjack.domain.state.Stay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayersTest {

    @Test
    @DisplayName("참가자 생성 성공")
    void createPlayersSucceed() {
        Players players = new Players(Arrays.asList(
                new Player(new Nickname("air")),
                new Player(new Nickname("picka"))
        ));

        assertThat(players.getPlayers()).hasSize(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "pika, ,air", ", , , ,"})
    @DisplayName("참가자 생성 실패")
    void createPlayersFail(String input) {
        assertThatThrownBy(() -> {
            List<Player> allPlayers = Arrays.stream(input.split(","))
                    .map(s -> s.replaceAll(" ", ""))
                    .map(Nickname::new)
                    .map(Player::new)
                    .collect(Collectors.toList());
            new Players(allPlayers);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("참가자 8명 초과 시 에러 발생")
    void checkPlayersMaximumCount() {
        assertThatThrownBy(() -> {
            List<Player> allPlayers = Arrays.stream("pika, air, a, b, c, d, e, f, g".split(","))
                    .map(s -> s.replaceAll(" ", ""))
                    .map(Nickname::new)
                    .map(Player::new)
                    .collect(Collectors.toList());
            new Players(allPlayers);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("중복된 참가자 에러 발생")
    void checkDuplicatePlayer() {
        assertThatThrownBy(() -> {
            new Players(Arrays.asList(
                    new Player(new Nickname("air")),
                    new Player(new Nickname("air"))
            ));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("참가자 2명 미만 시 에러 발생")
    void checkPlayersMinimumCount() {
        assertThatThrownBy(() -> {
            List<Player> allPlayers = Arrays.stream("air".split(","))
                    .map(s -> s.replaceAll(" ", ""))
                    .map(Nickname::new)
                    .map(Player::new)
                    .collect(Collectors.toList());
            new Players(allPlayers);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("최종 수익 계산")
    void calculateFinalProfit() {
        Dealer dealer = new Dealer(new Stay(new Cards(
                new Card(Shape.SPADE, Denomination.KING),
                new Card(Shape.CLOVER, Denomination.EIGHT)
        )));
        Player player1 = new Player(new Nickname("air"), new BlackJack(new Cards()));
        player1.betting(new BettingMoney("10000"));
        Player player2 = new Player(new Nickname("curry"), new Bust(new Cards()));
        player2.betting(new BettingMoney("20000"));
        Player player3 = new Player(new Nickname("jordan"), new Stay(new Cards(
                new Card(Shape.DIAMOND, Denomination.KING),
                new Card(Shape.HEART, Denomination.EIGHT)
        )));
        player3.betting(new BettingMoney("30000"));

        Players players = new Players(
                Arrays.asList(player1, player2, player3)
        );

        ProfitResult profitResult = players.calculateFinalProfit(dealer);
        Map<Participant, Profits> finalResult = profitResult.getProfitResult();
        assertEquals(finalResult.get(dealer), new Profits(new BigDecimal("5000")));
        assertEquals(finalResult.get(player1), new Profits(new BigDecimal("15000")));
        assertEquals(finalResult.get(player2), new Profits(new BigDecimal("-20000")));
        assertEquals(finalResult.get(player3), new Profits(new BigDecimal("0")));
    }
}
