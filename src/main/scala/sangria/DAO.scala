package sangria

import DBSchema._
import sangria.models.Link
import sangria.models.User
import sangria.models.Vote
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

class DAO(db: Database) {
  def allLinks = db.run(Links.result)

  def getLinks(ids: Seq[Int]) = db.run(
    Links.filter(_.id inSet ids).result
  )

  def getUsers(ids: Seq[Int]) = db.run(
    Users.filter(_.id inSet ids).result
  )

  def getVotes(ids: Seq[Int]) = db.run(
    Votes.filter(_.id inSet ids).result
  )
}


