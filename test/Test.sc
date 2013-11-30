import models._
import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import play.api.Play.current

object Test {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
   
   val cab=List(1,2,3,4,5,6,7,8)                  //> cab  : List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8)
   val levels=List(1,2,3,4,5,6,7,8,9)             //> levels  : List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
   val idealLevel1 =cab.indexOf(1)+1   *levels.size / (cab.size+1)
                                                  //> idealLevel1  : Int = 1
   val size = (10-Specs.minLevel)                 //> size  : Int = 10
   val cran = size/(cab.size+1)                   //> cran  : Int = 1
   
   val idealLevel2 =(cab.indexOf(8)+1)            //> idealLevel2  : Int = 8
   val test2=idealLevel2*cran                     //> test2  : Int = 8
}