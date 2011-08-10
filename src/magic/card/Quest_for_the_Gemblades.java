package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Quest_for_the_Gemblades {

	public static final MagicPermanentActivation V2390 =new MagicPermanentActivation(			"Quest for the Gemblades",
            new MagicCondition[]{MagicCondition.CHARGE_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.Charge,1),
				new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Put four +1/+1 counters on target creature$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,4,true));
			}
		}
	};
	
    public static final MagicTrigger V10210 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Quest for the Gemblades") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final MagicPlayer player=permanent.getController();
			final MagicSource source=damage.getSource();
			final MagicTarget target=damage.getTarget();
			if (damage.isCombat()&&source.getController()==player&&source.isPermanent()&&target.isPermanent()&&
				((MagicPermanent)source).isCreature()&&((MagicPermanent)target).isCreature()) {				
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Put a quest counter on Quest for the Gemblades.");			
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}
    };
    
}
