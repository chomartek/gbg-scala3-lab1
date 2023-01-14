package org.ditw.pise5.advent2022

object Day16 {
  val testInput = """Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
                    |Valve BB has flow rate=13; tunnels lead to valves CC, AA
                    |Valve CC has flow rate=2; tunnels lead to valves DD, BB
                    |Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
                    |Valve EE has flow rate=3; tunnels lead to valves FF, DD
                    |Valve FF has flow rate=0; tunnels lead to valves EE, GG
                    |Valve GG has flow rate=0; tunnels lead to valves FF, HH
                    |Valve HH has flow rate=22; tunnel leads to valve GG
                    |Valve II has flow rate=0; tunnels lead to valves AA, JJ
                    |Valve JJ has flow rate=21; tunnel leads to valve II""".stripMargin

  val input = """Valve NQ has flow rate=0; tunnels lead to valves SU, XD
                |Valve AB has flow rate=0; tunnels lead to valves XD, TE
                |Valve IA has flow rate=0; tunnels lead to valves CS, WF
                |Valve WD has flow rate=0; tunnels lead to valves DW, II
                |Valve XD has flow rate=10; tunnels lead to valves AB, NQ, VT, SC, MU
                |Valve SL has flow rate=0; tunnels lead to valves RP, DS
                |Valve FQ has flow rate=15; tunnels lead to valves EI, YC
                |Valve KF has flow rate=0; tunnels lead to valves FL, QP
                |Valve QP has flow rate=0; tunnels lead to valves KF, RP
                |Valve DS has flow rate=0; tunnels lead to valves SL, AA
                |Valve IK has flow rate=0; tunnels lead to valves XC, AA
                |Valve HQ has flow rate=0; tunnels lead to valves VM, WV
                |Valve WR has flow rate=0; tunnels lead to valves WV, HF
                |Valve HH has flow rate=20; tunnels lead to valves PI, CF, CN, NF, AR
                |Valve DW has flow rate=19; tunnels lead to valves KD, WD, HS
                |Valve RP has flow rate=14; tunnels lead to valves SL, QP, BH, LI, WP
                |Valve EC has flow rate=0; tunnels lead to valves NF, XC
                |Valve AA has flow rate=0; tunnels lead to valves NH, ES, UC, IK, DS
                |Valve VM has flow rate=18; tunnel leads to valve HQ
                |Valve NF has flow rate=0; tunnels lead to valves HH, EC
                |Valve PS has flow rate=0; tunnels lead to valves AR, SU
                |Valve IL has flow rate=0; tunnels lead to valves XC, KZ
                |Valve WP has flow rate=0; tunnels lead to valves CS, RP
                |Valve WF has flow rate=0; tunnels lead to valves FL, IA
                |Valve XW has flow rate=0; tunnels lead to valves OL, NL
                |Valve EH has flow rate=0; tunnels lead to valves UK, YR
                |Valve UC has flow rate=0; tunnels lead to valves AA, FL
                |Valve CS has flow rate=3; tunnels lead to valves IA, CN, LD, RJ, WP
                |Valve AR has flow rate=0; tunnels lead to valves PS, HH
                |Valve CF has flow rate=0; tunnels lead to valves HH, FL
                |Valve NH has flow rate=0; tunnels lead to valves AA, LD
                |Valve RJ has flow rate=0; tunnels lead to valves DJ, CS
                |Valve XC has flow rate=17; tunnels lead to valves IL, EC, YR, IK, DJ
                |Valve TE has flow rate=24; tunnels lead to valves AB, YA
                |Valve CN has flow rate=0; tunnels lead to valves HH, CS
                |Valve KD has flow rate=0; tunnels lead to valves DW, UK
                |Valve SC has flow rate=0; tunnels lead to valves EI, XD
                |Valve MU has flow rate=0; tunnels lead to valves XD, YP
                |Valve SU has flow rate=22; tunnels lead to valves PS, LI, II, NQ
                |Valve FL has flow rate=8; tunnels lead to valves KF, WF, CF, UC, HS
                |Valve OL has flow rate=4; tunnels lead to valves KZ, HF, XW
                |Valve EI has flow rate=0; tunnels lead to valves FQ, SC
                |Valve NL has flow rate=0; tunnels lead to valves XW, UK
                |Valve YP has flow rate=21; tunnels lead to valves YA, MU, YC
                |Valve BH has flow rate=0; tunnels lead to valves VT, RP
                |Valve II has flow rate=0; tunnels lead to valves SU, WD
                |Valve YA has flow rate=0; tunnels lead to valves TE, YP
                |Valve HS has flow rate=0; tunnels lead to valves FL, DW
                |Valve DJ has flow rate=0; tunnels lead to valves RJ, XC
                |Valve KZ has flow rate=0; tunnels lead to valves OL, IL
                |Valve YR has flow rate=0; tunnels lead to valves EH, XC
                |Valve UK has flow rate=7; tunnels lead to valves KD, NL, EH
                |Valve YC has flow rate=0; tunnels lead to valves FQ, YP
                |Valve ES has flow rate=0; tunnels lead to valves PI, AA
                |Valve LI has flow rate=0; tunnels lead to valves SU, RP
                |Valve LD has flow rate=0; tunnels lead to valves NH, CS
                |Valve VT has flow rate=0; tunnels lead to valves BH, XD
                |Valve PI has flow rate=0; tunnels lead to valves ES, HH
                |Valve WV has flow rate=11; tunnels lead to valves WR, HQ
                |Valve HF has flow rate=0; tunnels lead to valves OL, WR""".stripMargin
}
