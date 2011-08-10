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

public class Perimeter_Captain {

    public static final MagicTrigger V8443 =new MagicTrigger(MagicTriggerType.WhenBlocks,"Perimeter Captain") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (creature.getController()==player&&creature.hasAbility(game,MagicAbility.Defender)) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 2 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],2));
		}
    };
    
}
