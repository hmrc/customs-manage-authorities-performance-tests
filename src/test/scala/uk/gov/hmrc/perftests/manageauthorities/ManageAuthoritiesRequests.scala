/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.manageauthorities

import java.time.LocalDate

import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.manageauthorities.Requests._

trait ManageAuthoritiesRequests {
  self: PerformanceTestRunner with ServicesConfiguration =>

  private val baseUrl: String = baseUrlFor("customs-manage-authorities-frontend") + "/customs/manage-authorities"

  def dateToMap(date: LocalDate): Map[String, String] = {
    Map(
      "value.day" -> date.getDayOfMonth,
      "value.month" -> date.getMonthValue,
      "value.year" -> date.getYear
    ).mapValues(_.toString)
  }

  private val authorisedUserPayload: Map[String, String] = Map(
    "fullName" -> "name",
    "jobRole" -> "job",
    "confirmation" -> "true"
  )


  setup("add-journey", "Add journey") withRequests(
    getPage("Accounts page", saveToken = true, s"$baseUrl/add-authority/accounts"),
    postPage("Accounts Page", s"$baseUrl/add-authority/accounts", s"$baseUrl/add-authority/eori-number", Map("value[0]" -> "account_0")),
    postPage("EORI number Page", s"$baseUrl/add-authority/eori-number", s"$baseUrl/add-authority/start", "GB345834921000"),
    postPage("Start page", s"$baseUrl/add-authority/start", s"$baseUrl/add-authority/start-date", "setDate"),
    postPage("Start date Page", s"$baseUrl/add-authority/start-date", s"$baseUrl/add-authority/end", dateToMap(LocalDate.now().plusDays(1))),
    postPage("End page", s"$baseUrl/add-authority/end", s"$baseUrl/add-authority/end-date", "setDate"),
    postPage("End date page", s"$baseUrl/add-authority/end-date", s"$baseUrl/add-authority/available-balance", dateToMap(LocalDate.now().plusDays(2))),
    postPage("Available balance page", s"$baseUrl/add-authority/available-balance", s"$baseUrl/add-authority/check-answers", "yes"),
    postPage("Check your answers page", s"$baseUrl/add-authority/check-answers", s"$baseUrl/add-authority/confirmation", authorisedUserPayload)
  )
}
