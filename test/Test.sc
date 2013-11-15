import models._
import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import play.api.Play.current

object Test {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
   var B=new BuildingFile(models.Specs.minLevel,models.Specs.maxLevel)
                                                  //> B  : models.BuildingFile = models.BuildingFile@21669fc6
   var BF= B.getFloors                            //> BF  : Map[Int,List[models.Person]] = Map(0 -> List(), 5 -> List(), 10 -> Lis
                                                  //| t(), 14 -> List(), 1 -> List(), 6 -> List(), 9 -> List(), 13 -> List(), 2 ->
                                                  //|  List(), 17 -> List(), 12 -> List(), 7 -> List(), 3 -> List(), 18 -> List(),
                                                  //|  16 -> List(), 11 -> List(), 8 -> List(), 19 -> List(), 4 -> List(), 15 -> L
                                                  //| ist())
   val PersonClientMin=new Client(models.Specs.minLevel)
                                                  //> PersonClientMin  : models.Client = Client(0)
   val PersonClientMax=new Client(models.Specs.maxLevel)
                                                  //> PersonClientMax  : models.Client = Client(19)

	

	    B.reset()                             //> res0: scala.collection.immutable.Map[Int,List[Nothing]] = Map(0 -> List(), 5
                                                  //|  -> List(), 10 -> List(), 14 -> List(), 1 -> List(), 6 -> List(), 9 -> List(
                                                  //| ), 13 -> List(), 2 -> List(), 17 -> List(), 12 -> List(), 7 -> List(), 3 -> 
                                                  //| List(), 18 -> List(), 16 -> List(), 11 -> List(), 8 -> List(), 19 -> List(),
                                                  //|  4 -> List(), 15 -> List())
	    assert(B.size==0, "size 0")
	    BF=B.addPerson(2, PersonClientMin)
	    assert(B.size==1, "size 1")
	    BF=B.addPerson(2, PersonClientMin)
	    BF=B.addPerson(2, PersonClientMin)
	    BF=B.addPerson(2, PersonClientMin)
	    assert(B.size==4, "size 4")
	    BF=B.subPerson(2)
	    BF=B.subPerson(1)
	    B.subPerson(2)                        //> res1: Map[Int,List[models.Person]] = Map(0 -> List(), 5 -> List(), 10 -> Lis
                                                  //| t(), 14 -> List(), 1 -> List(), 6 -> List(), 9 -> List(), 13 -> List(), 2 ->
                                                  //|  List(Client(0), Client(0)), 17 -> List(), 12 -> List(), 7 -> List(), 3 -> L
                                                  //| ist(), 18 -> List(), 16 -> List(), 11 -> List(), 8 -> List(), 19 -> List(), 
                                                  //| 4 -> List(), 15 -> List())
	    B.subPerson(2)                        //> res2: Map[Int,List[models.Person]] = Map(0 -> List(), 5 -> List(), 10 -> Lis
                                                  //| t(), 14 -> List(), 1 -> List(), 6 -> List(), 9 -> List(), 13 -> List(), 2 ->
                                                  //|  List(Client(0)), 17 -> List(), 12 -> List(), 7 -> List(), 3 -> List(), 18 -
                                                  //| > List(), 16 -> List(), 11 -> List(), 8 -> List(), 19 -> List(), 4 -> List()
                                                  //| , 15 -> List())
	    assert(B.size==1, "size 1")
	    B.subPerson(2)                        //> res3: Map[Int,List[models.Person]] = Map(0 -> List(), 5 -> List(), 10 -> Lis
                                                  //| t(), 14 -> List(), 1 -> List(), 6 -> List(), 9 -> List(), 13 -> List(), 2 ->
                                                  //|  List(), 17 -> List(), 12 -> List(), 7 -> List(), 3 -> List(), 18 -> List(),
                                                  //|  16 -> List(), 11 -> List(), 8 -> List(), 19 -> List(), 4 -> List(), 15 -> L
                                                  //| ist())

}