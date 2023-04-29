package pl.jozwik.example.monad

import io.getquill.JdbcContextConfig
import io.getquill.util.LoadConfig
import pl.jozwik.example.PoolHelper

import javax.sql.DataSource

object TryHelperSpec {
  private val cfg      = JdbcContextConfig(LoadConfig(PoolHelper.PoolName))
  val pool: DataSource = cfg.dataSource
}
