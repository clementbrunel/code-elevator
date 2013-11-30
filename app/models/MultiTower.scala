package models
import models.DSL._
import tools.Log
import scala.collection.mutable.ListBuffer

case class MultiTower(name:String="MultiCabine") extends Algo{ 
  def display = (name -> name) 
  //Ideas of optimisation:
  //Set initial position differents and return when empty  => Done
  //Cabine dependants or ignore waiter if other cabine closer and good direction => other cabine closer win Done
   
   override def nextCommand(cabine:BuildingClients)= {
    val calculated=CalcNextCommand(cabine)
    if (BuildingWaiters.size()>(Specs.bestCapacity*(Specs.maxLevel- Specs.minLevel))){
      Log.warning("Size Waiters" +BuildingWaiters.size() + "vs "+ Specs.bestCapacity*(Specs.maxLevel- Specs.minLevel))
      Algo.resetManuAsked(CrashIsBetter())
    }
    calculated
  }
   
  def CalcNextCommand(cabine:BuildingClients):Command= {
    CrashDetection.addHelp("in MultiTower")
    var AlgoCabines = Algo.cabines

      if (BuildingWaiters.isEmpty && cabine.isEmpty){
	       cabine.state.door match {
	         case Closed()	=>goToGoodInitial(cabine,AlgoCabines)
	         case Opened()	=>Close
	       }
	    }
	    else {

	    	//CrashDetection.incrementCounter
		    val currentLevel=cabine.state.level
		    val happyWaiters= BuildingWaiters.levels.getOrElse(currentLevel, Nil)
		    val happyClients = cabine.levels.getOrElse(currentLevel, Nil)
		   (happyClients,happyWaiters,cabine.state.door) match {
		      case(clients,_,Closed()) if clients.size>0=>{
		    	  					CrashDetection.addHelp(" Clients a cet etage ")
						        	Log.debug("OPEN");  
						            Open
		      							}
		      case(_,waiters,Closed()) if waiters.size>0 && cabine.size<Specs.bestCapacity =>{
		      					if (!waiters.filter(waiter => (waiter.asInstanceOf[Waiter].direction.label==cabine.state.action.label)).isEmpty || cabine.isEmpty
		      					    ||cabine.state.level==Specs.minLevel ||cabine.state.level==Specs.maxLevel) {
		    	  				Log.debug("Enter in Optimisation with direction :" + cabine.state.action.label +" cabine.isEmpty " + cabine.isEmpty);  
	    	  					CrashDetection.addHelp(" Waiters in good direction so open ")
		    	  				Open
		      					}
		      					else{
		      					  Log.debug("list of waiter no stop because of direction" + happyWaiters + " vs " +cabine.state.action )
		      					  CrashDetection.addHelp(" in SmallTower no stop because of direction ")
		      					  cabine.state.action match {
		      					    case up:Up if cabine.state.level<Specs.maxLevel		=> Up //pour eviter le blocage initial si appel au niveau zero et etape actuel nothing
		      					    case down:Down if cabine.state.level>Specs.minLevel	=> Down
		      					    case others => cabine.state.calculDirection(cabine,AlgoCabines)  
		      					    }
		      					}
		      }
		  
		      case (_,_,Opened()) => {
		    	  				//BUG, les users ne rentrent pas si la porte est deja ouverte au bon etage quand ils arrivent
		    	  				Log.debug("Doors already opened, Come in but so do Nothing");  
		    	  				CrashDetection.addHelp("Close because was open")
	    					    Close	        					   
		        					  }
		      case (_,_,Closed()) => {
		    	  				Log.debug("Ferme et pas de client ou waiter a satisfaire")
		    	  				CrashDetection.addHelp("Pas de client ou waiter a satisfaire")
	    	  					cabine.state.calculDirection(cabine,AlgoCabines)
		      }
		    }
	    }
    }
  
  
  	def goToGoodInitial(currentCab:BuildingClients,cabines:List[BuildingClients]):Command={
  	  //initial Cabine is closed. We search the best level for waiting futures waiters
  	 val IndexOfThisCab = cabines.indexOf(currentCab)
     Log.debug("Index ox Current Cabine" + IndexOfThisCab)
     val idealLevel=(cabines.indexOf(currentCab)+1)*((Specs.maxLevel-Specs.minLevel) / (cabines.size+1))
     Log.debug("Ideal Level" + idealLevel)
     if (currentCab.state.level<idealLevel){Up}
     else if (currentCab.state.level>idealLevel) {Down}
     else{Nothing}
  	}
  
}