package blackjack.domain.participant;

import blackjack.domain.card.Card;
import blackjack.domain.card.Cards;
import blackjack.domain.card.Denomination;
import blackjack.domain.card.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static blackjack.domain.card.Cards.TOP_CARD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(new Nickname("pika"));
    }

    @Test
    @DisplayName("플레이어 생성 성공")
    void createPlayerSucceed() {
        assertThat(player).isEqualTo(new Player(new Nickname("pika")));
    }

    @Test
    @DisplayName("플레이어 카드 추가 성공")
    void receiveCard() {
        player.receiveCard(new Card(Shape.SPADE, Denomination.FOUR));
        Cards cards = player.getCurrentCards();
        assertThat(cards.getCards().get(TOP_CARD)).isEqualTo(new Card(Shape.SPADE, Denomination.FOUR));
    }

    @Test
    @DisplayName("플레이어 카드 반환 성공")
    void getPlayerCards() {
        player.receiveCard(new Card(Shape.SPADE, Denomination.FOUR));
        player.receiveCard(new Card(Shape.CLOVER, Denomination.THREE));
        player.receiveCard(new Card(Shape.HEART, Denomination.ACE));

        Cards cards = player.getCurrentCards();
        assertTrue(cards.getCards().containsAll(Arrays.asList(new Card(Shape.SPADE, Denomination.FOUR),
                new Card(Shape.CLOVER, Denomination.THREE),
                new Card(Shape.HEART, Denomination.ACE))));
    }

    @Test
    @DisplayName("카드를 더 뽑을 수 있는지 확인")
    void canDraw() {
        player.receiveCard(new Card(Shape.DIAMOND, Denomination.ACE));
        player.receiveCard(new Card(Shape.DIAMOND, Denomination.THREE));
        player.receiveCard(new Card(Shape.DIAMOND, Denomination.KING));
        assertTrue(player.canDraw());
    }

    @Test
    @DisplayName("카드를 뽑을 수 없는 경우 확인")
    void cannotDraw() {
        player.receiveCard(new Card(Shape.DIAMOND, Denomination.KING));
        player.receiveCard(new Card(Shape.DIAMOND, Denomination.THREE));
        player.receiveCard(new Card(Shape.DIAMOND, Denomination.JACK));
        assertFalse(player.canDraw());
    }
}