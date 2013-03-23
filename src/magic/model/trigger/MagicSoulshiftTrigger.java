package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTarget;

public class MagicSoulshiftTrigger extends MagicWhenPutIntoGraveyardTrigger {
    
    private final int cmc;
    
    public MagicSoulshiftTrigger(final int cmc) {
        this.cmc = cmc;
    }
    
    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicGraveyardTriggerData triggerData) {
        if (triggerData.fromLocation == MagicLocationType.Play) {
            final MagicTargetFilter<MagicCard> targetFilter =
                    new MagicTargetFilter.MagicCMCCardFilter(
                        MagicTargetFilter.TARGET_SPIRIT_CARD_FROM_GRAVEYARD,
                        MagicTargetFilter.Operator.LESS_THAN_OR_EQUAL,
                        cmc
                    );
            final MagicTargetChoice targetChoice = 
                    new MagicTargetChoice(
                    targetFilter,false,MagicTargetHint.None,
                    "a Spirit card from your graveyard)");
            return new MagicEvent(
                    permanent,
                    new MagicMayChoice(targetChoice),
                    new MagicGraveyardTargetPicker(false),
                    this,
                    "PN may$ return target Spirit card$ with " +
                    "converted mana cost " + cmc + " or less " +
                    "from his or her graveyard to his or her hand.");
        }
        return MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        if (event.isYes()) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicRemoveCardAction(
                        card,
                        MagicLocationType.Graveyard
                    ));
                    game.doAction(new MagicMoveCardAction(
                        card,
                        MagicLocationType.Graveyard,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    }
}
