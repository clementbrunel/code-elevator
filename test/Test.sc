import models._
import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import play.api.Play.current

object Test {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
   
   val cab=List(1,2,3,4,5,6,7,8)                  //> cab  : List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8)
   val levels=(-10 to 40).toList                  //> levels  : List[Int] = List(-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2,
                                                  //|  3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23
                                                  //| , 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40)
   val cran:Double = levels.size.toDouble/(cab.size+1)
                                                  //> cran  : Double = 5.666666666666667
   
   val idealLevel2 =(cab.indexOf(8)+1)            //> idealLevel2  : Int = 8
	 val test2=(-10) + idealLevel2*cran.round.toInt
                                                  //> test2  : Int = 38
}