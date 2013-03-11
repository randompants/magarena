[
    new MagicPermanentActivation(
        [MagicCondition.ONE_SAPROLING_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Pump") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.SACRIFICE_SAPROLING
                )
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +1/+2 until end of turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(), 1, 2));
        }
    }
]
