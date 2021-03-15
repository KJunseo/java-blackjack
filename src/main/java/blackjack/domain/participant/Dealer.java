package blackjack.domain.participant;

import blackjack.domain.result.MatchResult;
import blackjack.domain.state.State;

public class Dealer extends Participant {
    public static final Nickname DEALER_NAME = new Nickname("딜러");

    public Dealer() {
        super(DEALER_NAME);
    }

    public Dealer(State state) {
        super(DEALER_NAME, state);
    }

    public MatchResult matchGame(Player player) {
        return MatchResult.getPlayerMatchResult(player.state, state);
    }

    @Override
    public boolean canDraw() {
        return !state.isFinished() && state.getCards().lessThanSixteen();
    }
}
