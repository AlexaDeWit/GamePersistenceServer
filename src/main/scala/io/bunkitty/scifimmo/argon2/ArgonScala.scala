package io.bunkitty.scifimmo.argon2

import io.bunkitty.scifimmo.argon2.ArgonScala.RawPassword
import de.mkammerer.argon2.{Argon2, Argon2Factory}


case class HashedPassword private(underlying: String) {
  def verify(rawPassword: RawPassword): Boolean = ArgonScala.verify(rawPassword, this)

  override def toString: RawPassword = underlying
}

object HashedPassword {
  def unsafeWrapString(hashed: String) = HashedPassword(hashed)
}


object ArgonScala {
  type RawPassword = String
  lazy val argon2: Argon2 = Argon2Factory.create()
  private lazy val partialHashFunction: RawPassword => String = argon2.hash(2, 65536, 1, _)
  lazy val hashFunction: RawPassword => HashedPassword = raw => HashedPassword(partialHashFunction(raw))

  def verify(raw: RawPassword, hash: HashedPassword): Boolean = argon2.verify(hash.underlying, raw)
  def hashPassword(raw: RawPassword): HashedPassword = hashFunction(raw)
}
