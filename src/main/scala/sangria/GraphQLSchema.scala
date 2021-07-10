package sangria

import akka.http.scaladsl.model.DateTime
import sangria.schema.{Field, ListType, ObjectType}
import sangria.macros.derive._
import models._
import sangria.ast.StringValue
import sangria.execution.deferred.{DeferredResolver, Fetcher, HasId}
import sangria.schema._

object GraphQLSchema {

  implicit val GraphQLDateTime = ScalarType[DateTime](//1
    "DateTime",//2
    coerceOutput = (dt, _) => dt.toString, //3
    coerceInput = { //4
      case StringValue(dt, _, _, _, _) => DateTime.fromIsoDateTimeString(dt).toRight(DateTimeCoerceViolation)
      case _ => Left(DateTimeCoerceViolation)
    },
    coerceUserInput = { //5
      case s: String => DateTime.fromIsoDateTimeString(s).toRight(DateTimeCoerceViolation)
      case _ => Left(DateTimeCoerceViolation)
    }
  )

  val IdentifiableType = InterfaceType(
    "Identifiable",
    fields[Unit, Identifiable](
      Field("id", IntType, resolve = _.value.id)
    )
  )

  implicit val LinkType = deriveObjectType[Unit, Link](
    Interfaces(IdentifiableType)
  )
  implicit val UserType = deriveObjectType[Unit, User](
    Interfaces(IdentifiableType)
  )
  implicit val VoteType = deriveObjectType[Unit, Vote](
    Interfaces(IdentifiableType)
  )

  val linksFetcher = Fetcher(
    (ctx: MyContext, ids: Seq[Int]) => ctx.dao.getLinks(ids)
  )

  val usersFetcher = Fetcher(
    (ctx: MyContext, ids: Seq[Int]) => ctx.dao.getUsers(ids)
  )

  val votesFetcher = Fetcher(
    (ctx: MyContext, ids: Seq[Int]) => ctx.dao.getVotes(ids)
  )

  val Resolver = DeferredResolver.fetchers(linksFetcher, usersFetcher, votesFetcher)

  val Id = Argument("id", IntType)
  val Ids = Argument("ids", ListInputType(IntType))

  val QueryType = ObjectType(
    "Query",
    fields[MyContext, Unit](
      Field("allLinks", ListType(LinkType), resolve = c => c.ctx.dao.allLinks),
      Field("link",
        OptionType(LinkType),
        arguments = Id :: Nil,
        resolve = c => linksFetcher.deferOpt(c.arg(Id))
      ),
      Field("links",
        ListType(LinkType),
        arguments = List(Argument("ids", ListInputType(IntType))),
        resolve = c => linksFetcher.deferSeq(c.arg(Ids))
      ),
      Field("users",
        ListType(UserType),
        arguments = List(Ids),
        resolve = c => usersFetcher.deferSeq(c.arg(Ids))
      ),
      Field("votes",
        ListType(VoteType),
        arguments = List(Ids),
        resolve = c => votesFetcher.deferSeq(c.arg(Ids))
      )
    )
  )

  // 3
  val SchemaDefinition = Schema(QueryType)
}