package tools


object Timer{
	  def chrono[A](f: => A) = {
	  val s = System.nanoTime
	  val ret = f
	  Log.debug("time: "+(System.nanoTime-s)/1e6+"ms")
	  ret
	}
}