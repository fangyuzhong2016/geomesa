/***********************************************************************
 * Copyright (c) 2013-2020 Commonwealth Computer Research, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at
 * http://www.opensource.org/licenses/apache2.0.php.
 ***********************************************************************/

package org.locationtech.geomesa.spark.jts.udf

import org.locationtech.jts.geom.{Geometry, Polygon}
import org.apache.spark.sql.functions._

import org.locationtech.geomesa.spark.jts.util.WKTUtils
import org.locationtech.geomesa.spark.jts._

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GeometricProcessingFunctionsTest extends Specification with TestEnvironment {

  "sql geometry constructors" should {
    sequential

    // before
    step {
      // Trigger initialization of spark session
      val _ = spark
    }
    "st_bufferPoint" >> {

      "should handle nulls" >> {
        sc.sql("select st_bufferPoint(null, null)").collect.head(0) must beNull
        dfBlank.select(st_bufferPoint(lit(null), lit(null))).first must beNull
      }

      "should return a point buffered in meters" >> {
        val buf = sc.sql("select st_bufferPoint(st_makePoint(0,0), 10)").collect().head.get(0)
        val dfBuf = dfBlank.select(st_bufferPoint(st_makePoint(lit(0),lit(0)), lit(10))).first

        val bufferedPoly = WKTUtils.read(
          """
          |POLYGON ((0.0000899320367762 0, 0.0000897545764446 0.0000056468793115, 0.0000892228958048 0.0000112714729702, 0.0000883390931573 0.0000168515832745, 0.0000871066564674 0.0000223651880784, 0.0000855304495997 0.0000277905277026, 0.0000836166931225 0.0000331061908102, 0.0000813729397584 0.0000382911989076, 0.0000788080445769 0.0000433250891364, 0.0000759321300474 0.0000481879950317, 0.0000727565460907 0.0000528607249257, 0.0000692938252858 0.0000573248376881, 0.0000655576334099 0.0000615627155054, 0.0000615627155054 0.0000655576334099, 0.0000573248376881 0.0000692938252858, 0.0000528607249257 0.0000727565460907, 0.0000481879950317 0.0000759321300474, 0.0000433250891364 0.0000788080445769, 0.0000382911989076 0.0000813729397584, 0.0000331061908102 0.0000836166931225, 0.0000277905277026 0.0000855304495997, 0.0000223651880784 0.0000871066564674, 0.0000168515832745 0.0000883390931573, 0.0000112714729702 0.0000892228958048, 0.0000056468793115 0.0000897545764446, -0 0.0000899320367762, -0.0000056468793115 0.0000897545764446, -0.0000112714729702 0.0000892228958048, -0.0000168515832745 0.0000883390931573, -0.0000223651880784 0.0000871066564674, -0.0000277905277026 0.0000855304495997, -0.0000331061908102 0.0000836166931225, -0.0000382911989076 0.0000813729397584, -0.0000433250891364 0.0000788080445769, -0.0000481879950317 0.0000759321300474, -0.0000528607249257 0.0000727565460907, -0.0000573248376881 0.0000692938252858, -0.0000615627155054 0.0000655576334099, -0.0000655576334099 0.0000615627155054, -0.0000692938252858 0.0000573248376881, -0.0000727565460907 0.0000528607249257, -0.0000759321300474 0.0000481879950317, -0.0000788080445769 0.0000433250891364, -0.0000813729397584 0.0000382911989076, -0.0000836166931225 0.0000331061908102, -0.0000855304495997 0.0000277905277026, -0.0000871066564674 0.0000223651880784, -0.0000883390931573 0.0000168515832745, -0.0000892228958048 0.0000112714729702, -0.0000897545764446 0.0000056468793115, -0.0000899320367762 -0, -0.0000897545764446 -0.0000056468793115, -0.0000892228958048 -0.0000112714729702, -0.0000883390931573 -0.0000168515832745, -0.0000871066564674 -0.0000223651880784, -0.0000855304495997 -0.0000277905277026, -0.0000836166931225 -0.0000331061908102, -0.0000813729397584 -0.0000382911989076, -0.0000788080445769 -0.0000433250891364, -0.0000759321300474 -0.0000481879950317, -0.0000727565460907 -0.0000528607249257, -0.0000692938252858 -0.0000573248376881, -0.0000655576334099 -0.0000615627155054, -0.0000615627155054 -0.0000655576334099, -0.0000573248376881 -0.0000692938252858, -0.0000528607249257 -0.0000727565460907, -0.0000481879950317 -0.0000759321300474, -0.0000433250891364 -0.0000788080445769, -0.0000382911989076 -0.0000813729397584, -0.0000331061908102 -0.0000836166931225, -0.0000277905277026 -0.0000855304495997, -0.0000223651880784 -0.0000871066564674, -0.0000168515832745 -0.0000883390931573, -0.0000112714729702 -0.0000892228958048, -0.0000056468793115 -0.0000897545764446, -0 -0.0000899320367762, 0.0000056468793115 -0.0000897545764446, 0.0000112714729702 -0.0000892228958048, 0.0000168515832745 -0.0000883390931573, 0.0000223651880784 -0.0000871066564674, 0.0000277905277026 -0.0000855304495997, 0.0000331061908102 -0.0000836166931225, 0.0000382911989076 -0.0000813729397584, 0.0000433250891364 -0.0000788080445769, 0.0000481879950317 -0.0000759321300474, 0.0000528607249257 -0.0000727565460907, 0.0000573248376881 -0.0000692938252858, 0.0000615627155054 -0.0000655576334099, 0.0000655576334099 -0.0000615627155054, 0.0000692938252858 -0.0000573248376881, 0.0000727565460907 -0.0000528607249257, 0.0000759321300474 -0.0000481879950317, 0.0000788080445769 -0.0000433250891364, 0.0000813729397584 -0.0000382911989076, 0.0000836166931225 -0.0000331061908102, 0.0000855304495997 -0.0000277905277026, 0.0000871066564674 -0.0000223651880784, 0.0000883390931573 -0.0000168515832745, 0.0000892228958048 -0.0000112714729702, 0.0000897545764446 -0.0000056468793115, 0.0000899320367762 0))
        """.stripMargin)

        buf.asInstanceOf[Polygon].equalsExact(bufferedPoly, 0.000001) must beTrue
        dfBuf.asInstanceOf[Polygon].equalsExact(bufferedPoly, 0.000001) must beTrue
      }

      "should handle antimeridian" >> {
        val buf = sc.sql("select st_bufferPoint(st_makePoint(-180, 50), 100000)").first.getAs[Geometry](0)
        val dfBuf = dfBlank.select(st_bufferPoint(st_makePoint(lit(-180), lit(50)), lit(100000))).first
        // check points on both sides of antimeridian
        buf.contains(WKTUtils.read("POINT(-179.9 50)")) mustEqual true
        buf.contains(WKTUtils.read("POINT(179.9 50)")) mustEqual true

        dfBuf.contains(WKTUtils.read("POINT(-179.9 50)")) mustEqual true
        dfBuf.contains(WKTUtils.read("POINT(179.9 50)")) mustEqual true
      }
    }

    "st_antimeridianSafeGeom" >> {
      "should handle nulls" >> {
        sc.sql("select st_antimeridianSafeGeom(null)").collect.head(0) must beNull
        dfBlank.select(st_antimeridianSafeGeom(lit(null))).first must beNull
      }

      "should split a geom that spans the antimeridian" >> {

        val wkt = "POLYGON((-190 50, -190 60, -170 60, -170 50, -190 50))"
        val geom = s"st_geomFromWKT('$wkt')"
        val decomposed = sc.sql(s"select st_antimeridianSafeGeom($geom)").first.getAs[Geometry](0)
        val dfDecomposed = dfBlank.select(st_antimeridianSafeGeom(st_geomFromWKT(lit(wkt)))).first

        val expected = WKTUtils.read(
          "MULTIPOLYGON (((-180 50, -180 60, -170 60, -170 50, -180 50)), ((180 60, 180 50, 170 50, 170 60, 180 60)))")

        decomposed mustEqual expected
        dfDecomposed mustEqual expected
      }
    }
  }
}
