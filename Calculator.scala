import scala.io.StdIn.readLine
import scala.util.Try

// --- 1. Definirea tipurilor de date (ADT) ---
sealed trait Expr
case class Num(v: Double) extends Expr
case class BinOp(op: String, e1: Expr, e2: Expr) extends Expr
case class UnOp(op: String, e: Expr) extends Expr

object Calculator {

  // --- 2. Parsare ---
  // Transformam un string intr-o lista de tokeni, apoi construim arborele
  def parseTokens(tokens: List[String]): Either[String, (Expr, List[String])] = tokens match {
    case Nil => Left("Expresie incompleta.")
    case "(" :: op :: rest if Set("+", "-", "*", "/").contains(op) =>
      for {
        res1 <- parseTokens(rest)
        (e1, rest1) = res1
        res2 <- parseTokens(rest1)
        (e2, rest2) = res2
        res3 <- rest2 match {
          case ")" :: tail => Right((BinOp(op, e1, e2), tail))
          case _ => Left("Eroare de sintaxa: lipseste ')'.")
        }
      } yield res3
    case "(" :: op :: rest if Set("sqrt", "neg").contains(op) =>
      for {
        res <- parseTokens(rest)
        (e, rest1) = res
        res2 <- rest1 match {
          case ")" :: tail => Right((UnOp(op, e), tail))
          case _ => Left("Eroare de sintaxa: lipseste ')'.")
        }
      } yield res2
    case v :: rest => 
      Try(v.toDouble).toEither.left.map(_ => s"Token invalid: $v")
        .map(num => (Num(num), rest))
  }

  def parseaza(input: String): Either[String, Expr] = {
    val tokens = input.replace("(", " ( ").replace(")", " ) ").trim.split("\\s+").toList.filter(_.nonEmpty)
    parseTokens(tokens).flatMap {
      case (expr, Nil) => Right(expr)
      case (_, _) => Left("Eroare: Tokeni extra la finalul expresiei.")
    }
  }

  // --- 3. Evaluare (folosind Either pentru erori) ---
  def evalExpresie(ast: Expr): Either[String, Double] = ast match {
    case Num(v) => Right(v)
    case BinOp("+", e1, e2) => for (v1 <- evalExpresie(e1); v2 <- evalExpresie(e2)) yield v1 + v2
    case BinOp("-", e1, e2) => for (v1 <- evalExpresie(e1); v2 <- evalExpresie(e2)) yield v1 - v2
    case BinOp("*", e1, e2) => for (v1 <- evalExpresie(e1); v2 <- evalExpresie(e2)) yield v1 * v2
    case BinOp("/", e1, e2) => 
      for {
        v1 <- evalExpresie(e1)
        v2 <- evalExpresie(e2)
        res <- if (v2 == 0) Left("Eroare: Impartire la zero!") else Right(v1 / v2)
      } yield res
    case UnOp("sqrt", e) =>
      evalExpresie(e).flatMap(v => if (v < 0) Left("Radical din numar negativ!") else Right(Math.sqrt(v)))
    case UnOp("neg", e) => evalExpresie(e).map(v => -v)
    case _ => Left("Operatie necunoscuta.")
  }

  // --- 4. Vizualizare (Infix) ---
  def afiseazaExpresie(ast: Expr): String = ast match {
    case Num(v) => if (v % 1 == 0) v.toInt.toString else v.toString
    case BinOp(op, e1, e2) => s"(${afiseazaExpresie(e1)} $op ${afiseazaExpresie(e2)})"
    case UnOp("sqrt", e) => s"sqrt(${afiseazaExpresie(e)})"
    case UnOp("neg", e) => s"(-${afiseazaExpresie(e)})"
  }

  // --- 5. REPL ---
  def repl(): Unit = {
    print("> ")
    val input = readLine()
    if (input != null && input.trim.toLowerCase != "exit") {
      parseaza(input) match {
        case Left(err) => println(err)
        case Right(ast) =>
          val infix = afiseazaExpresie(ast)
          evalExpresie(ast) match {
            case Left(err) => println(s"Infix: $infix\n$err")
            case Right(rez) => println(s"Infix: $infix\nRezultat: $rez")
          }
      }
      repl()
    }
  }

  def main(args: Array[String]): Unit = {
    println("Calculator Prefix. Scrie 'exit' pentru iesire. Ex: (+ 3 (* 4 5))")
    repl()
  }
}