def INSTANT_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Instant);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
};

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                INSTANT_FROM_HAND,  
                MagicTargetHint.None,
                "an instant card to exile from your hand"
            );
            return new MagicEvent(
                permanent,
                new MagicMayChoice(targetChoice),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN may\$ exile an instant card\$ from his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard target) {
                        game.doAction(new MagicExileUntilThisLeavesPlayAction(
                            event.getPermanent(), 
                            target, 
                            MagicLocationType.OwnersHand
                        ));
                    }
                });
            }
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return (permanent.getEquippedCreature() == damage.getSource() &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    permanent.getExiledCard(),
                    this,
                    "PN may\$ cast a copy of " + permanent.getExiledCard().getName() + " without paying its mana cost."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicCastFreeCopyAction(
                event.getPlayer(), 
                event.getRefCard()
            ));
            }
        }
    }
]
