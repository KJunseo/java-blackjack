package blackjack.domain.state;

import blackjack.domain.card.Card;
import blackjack.domain.card.Cards;
import blackjack.domain.money.BettingMoney;
import blackjack.domain.money.Profits;

import java.math.BigDecimal;

public abstract class Finished extends Started {
    private static final String CANNOT_DRAW_CARD_ERROR_MESSAGE = "카드를 뽑을 수 없는 상태입니다.";

    public Finished(Cards cards) {
        super(cards);
    }

    public abstract BigDecimal rate();

    @Override
    public final Profits profit(BettingMoney money) {
        return money.multiply(rate());
    }

    @Override
    public final boolean isFinished() {
        return true;
    }

    @Override
    public final State draw(Card card) {
        throw new IllegalStateException(CANNOT_DRAW_CARD_ERROR_MESSAGE);
    }

    @Override
    public final State stay() {
        throw new IllegalStateException();
    }
}
