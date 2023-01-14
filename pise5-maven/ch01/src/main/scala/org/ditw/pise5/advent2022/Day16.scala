package org.ditw.pise5.advent2022

object Day16 {
  case class FlowSum(moveSteps: Int, flow: Int) {
    def calc(time: Int): Int = {
      if (time > moveSteps + 1) {
        (time - moveSteps - 1) * flow
      } else {
        0
      }
    }
  }
}
