package pl.jozwik.example.cassandra

import io.getquill.{ CassandraSyncContext, SnakeCase }

trait AbstractSyncSpec extends AbstractCassandraSpec {
  protected lazy val ctx = new CassandraSyncContext(SnakeCase, "cassandra")
}
