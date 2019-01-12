package com.bkushigian.unroll;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.LinkedList;
import java.util.List;

public class UnrollVisitor extends VoidVisitorAdapter<Void> {

  int depth = 32;

  public UnrollVisitor(int depth) {
    this.depth = depth;
  }

  @Override
  public void visit(WhileStmt n, Void arg) {
    super.visit(n, arg);
    final Statement body = n.getBody();
    final Node parent = n.getParentNode().orElseThrow(() -> new RuntimeException("No parent node"));
    final Expression cond = n.getCondition();
    final BlockStmt replacementBlock = new BlockStmt();
    final BlockStmt block = body.isBlockStmt() ? (BlockStmt) body
                                               : (new BlockStmt()).addStatement(body);
    replacementBlock.addStatement(unroll(cond, block, getReturnStmt(-1), depth));
    parent.replace(n, replacementBlock);
  }

  @Override
  public void visit(ForEachStmt n, Void arg) {
    super.visit(n, arg);
  }

  @Override
  public void visit(ForStmt n, Void arg) {
    super.visit(n, arg);
    final Statement body = n.getBody();
    final Node parent = n.getParentNode().orElseThrow(() -> new RuntimeException("No parent node"));
    final Expression cond = n.getCompare().orElse(new BooleanLiteralExpr(true));
    final BlockStmt replacementBlock = new BlockStmt();

    for (Expression expr : n.getInitialization()) {
      replacementBlock.addStatement(new ExpressionStmt(expr));
    }
    final BlockStmt block = body.isBlockStmt() ? (BlockStmt) body
                                               : (new BlockStmt()).addStatement(body);
    for (Expression expr : n.getUpdate()) {
      block.addStatement(expr);
    }
    replacementBlock.addStatement(unroll(cond, block, getReturnStmt(-1), depth));
    parent.replace(n, replacementBlock);
  }

  private ThrowStmt getThrowStmt(final String type, final String msg) {
    final ThrowStmt throwStmt = new ThrowStmt();
    final ObjectCreationExpr exception = new ObjectCreationExpr();
    exception.setType(type);
    final NodeList<Expression> args = new NodeList<>();
    args.add(new StringLiteralExpr(msg));
    exception.setArguments(args);
    throwStmt.setExpression(exception);
    return throwStmt;
  }

  private ReturnStmt getReturnStmt(final Integer lit) {
    return (new ReturnStmt()).setExpression(new IntegerLiteralExpr(lit));
  }

  private ReturnStmt getReturnStmt(final Boolean lit) {
    return (new ReturnStmt()).setExpression(new BooleanLiteralExpr(lit));
  }

  private Statement unroll(final Expression cond,
      final Statement then,
      final Statement bot,
      final int depth)
  {
    final IfStmt ifStmt = new IfStmt();
    ifStmt.setCondition(cond);
    if (depth <= 0) {
      ifStmt.setThenStmt(bot);
    } else {
      final BlockStmt block = new BlockStmt();
      block.addStatement(then);
      block.addStatement(unroll(cond, then, bot, depth - 1));
      ifStmt.setThenStmt(block);
    }
    return ifStmt;
  }
}
