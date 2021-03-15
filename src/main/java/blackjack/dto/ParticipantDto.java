package blackjack.dto;

import blackjack.domain.card.Card;
import blackjack.domain.card.Cards;
import blackjack.domain.participant.Nickname;
import blackjack.domain.participant.Participant;

import java.util.List;

public class ParticipantDto {
    private final Nickname name;
    private final Cards cards;
    private final int finalScore;

    public ParticipantDto(Participant participant) {
        this.name = participant.getName();
        this.cards = participant.getCurrentCards();
        this.finalScore = cards.calculateScore();
    }

    public String getName() {
        return name.getName();
    }

    public List<Card> getCards() {
        return cards.getCards();
    }

    public int getFinalScore() {
        return finalScore;
    }
}
