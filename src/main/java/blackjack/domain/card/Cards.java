package blackjack.domain.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static blackjack.controller.BlackJackGame.BLACKJACK_NUMBER;

public class Cards {
    private static final String NO_REMAIN_CARD_ERROR_MESSAGE = "남은 카드가 없습니다.";
    private static final int REMAIN_ACE_COUNT = 10;
    private static final int TOP_CARD = 0;

    private final List<Card> cards;

    public Cards() {
        this(Collections.emptyList());
    }

    public Cards(Card card) {
        this(Collections.singletonList(card));
    }

    public Cards(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public Cards addCard(Card card) {
        List<Card> newCards = new ArrayList<>(cards);
        newCards.add(card);
        return new Cards(newCards);
    }

    public Card peekCard() {
        return cards.get(TOP_CARD);
    }

    public Cards removeCard() {
        if (cards.size() == 0) {
            throw new IndexOutOfBoundsException(NO_REMAIN_CARD_ERROR_MESSAGE);
        }
        List<Card> newCards = new ArrayList<>(cards);
        newCards.remove(TOP_CARD);
        return new Cards(newCards);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int calculateScore() {
        int score = cards.stream()
                .mapToInt(Card::score)
                .sum();
        long aceCount = cards.stream()
                .filter(Card::isAce)
                .count();
        for (int i = 0; i < aceCount; i++) {
            score = plusRemainAceScore(score);
        }
        return score;
    }

    private int plusRemainAceScore(int score) {
        if (score + REMAIN_ACE_COUNT <= BLACKJACK_NUMBER) {
            score += REMAIN_ACE_COUNT;
        }
        return score;
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
