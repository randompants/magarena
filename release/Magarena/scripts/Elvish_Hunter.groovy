[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Tapping),
        "BlockUntap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source,"{1}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE,
                new MagicNoCombatTargetPicker(true,true,false),
                this,
                "Target creature\$ doesn't untap during its controller's next untap step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(MagicChangeStateAction.Set(
                        creature,
                        MagicPermanentState.DoesNotUntapDuringNext
                    ));
                }
            });
        }
    }
]
