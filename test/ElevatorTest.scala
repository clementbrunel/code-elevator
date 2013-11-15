
import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import play.api.Play.current
import models._

@RunWith(classOf[JUnitRunner])
class ElevatorTest extends FunSuite{
   var B=new BuildingFile(models.Specs.minLevel,models.Specs.maxLevel)
   var BF= B.getFloors
   val PersonClientMin=new Client(models.Specs.minLevel)
   val PersonClientMax=new Client(models.Specs.maxLevel)
	test("Test the Unit test module") {
	    assert(1 === 1)
	 }

	test("Levels Size"){
	    B.reset()
	    assert(B.size===0, "size 0")
	    BF=B.addPerson(2, PersonClientMin)
	    assert(B.size===1, "size 1")
	    BF=B.addPerson(2, PersonClientMin)
	    BF=B.addPerson(2, PersonClientMin)
	    BF=B.addPerson(2, PersonClientMin)
	    assert(B.size===4, "size 2")
	    BF=B.subPerson(2)
	    BF=B.subPerson(1)
	    B.subPerson(2)
	    B.subPerson(2)
	    assert(B.size===1, "size 3")
	    B.subPerson(2)
	    assert(B.size===0, "size 4")
	    BF=B.addPerson(models.Specs.minLevel, PersonClientMin)
	    BF=B.addPerson(models.Specs.maxLevel, PersonClientMax)
	    BF=B.addPerson(models.Specs.maxLevel, PersonClientMax)
	    assert(B.size===3, "size 5")
	    B.subPerson(models.Specs.maxLevel)
	    assert(B.size===2, "size 6")
	}
	
	test("Reset"){
	   B.reset()
	   BF=B.addPerson(2, PersonClientMin)
	   BF=B.addPerson(models.Specs.maxLevel, PersonClientMax)
	   B.reset()
	   assert(B.size===0, "Reset 0")
	}
	
}

