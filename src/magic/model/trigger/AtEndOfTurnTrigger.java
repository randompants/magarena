package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.RemoveFromPlayAction;
import magic.model.action.SacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class AtEndOfTurnTrigger extends MagicTrigger<MagicPlayer> {
    public AtEndOfTurnTrigger(final int priority) {
        super(priority);
    }

    public AtEndOfTurnTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtEndOfTurn;
    }

    public static final AtEndOfTurnTrigger create(final MagicSourceEvent sourceEvent) {
        return new AtEndOfTurnTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    public static final AtEndOfTurnTrigger createYour(final MagicSourceEvent sourceEvent) {
        return new AtEndOfTurnTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
                return sourceEvent.getEvent(permanent);
            }
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer eotPlayer) {
                return permanent.isController(eotPlayer);
            }
        };
    }

    public static final AtEndOfTurnTrigger Sacrifice = new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new SacrificeAction(event.getPermanent()));
        }
    };

    public static final AtEndOfTurnTrigger ExileAtEnd = new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
        }
    };

    public static final AtEndOfTurnTrigger ExileAtYourEnd(final MagicPlayer your) {
        return new AtEndOfTurnTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
                return your.getId() == eotPlayer.getId() ?
                    new MagicEvent(
                        permanent,
                        this,
                        "Exile SN."
                    ):
                    MagicEvent.NONE;
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
            }
        };
    };

    public static final AtEndOfTurnTrigger Return = new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eocPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Return SN to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveFromPlayAction(
                event.getPermanent(),
                MagicLocationType.OwnersHand
            ));
        }
    };
}