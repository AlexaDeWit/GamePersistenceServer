package io.bunkitty.scifimmo.jwt

import org.scalatest.{FreeSpec, Matchers}

class JwtServiceSpec extends FreeSpec with Matchers {

  "Jwt Service" - {
    "Should generate the glorious example from jwt lib's example" in {
      val signedIo = JwtService("secretKey").sign("""{"user":1}""")
      signedIo.unsafeRunSync() shouldBe "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoxfQ.oG3iKnAvj_OKCv0tchT90sv2IFVeaREgvJmwgRcXfkI"
    }

    "Should be able to validate a valid JWT from jwt.io" in {
      val verification = JwtService("secret").verify("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.XbPfbIHMI6arZ3Y922BhjWgQzWXcXNrz0ogtVhfEd2o")
      verification.unsafeRunSync() shouldBe true
    }

    "Should correclty encode jwt.io's example" in {
      val signedIo = JwtService("secret").sign("""{"sub":"1234567890","name":"John Doe","iat":1516239022}""")
      signedIo.unsafeRunSync() shouldBe  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.7m6JhjDj0Blnye6rLAat5mX0BCivb9XXuEY15LprW8c"

    }
  }

}
