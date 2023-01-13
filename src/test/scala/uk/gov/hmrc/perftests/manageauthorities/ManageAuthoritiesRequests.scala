/*
 * Copyright 2023 HM Revenue & Customs
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
      "value.day" -> s"${date.getDayOfMonth}",
      "value.month" -> s"${date.getMonthValue}",
      "value.year" -> s"${date.getYear}"
    )
  }

  private val authorisedUserPayload: Map[String, String] = Map(
    "fullName" -> "name",
    "jobRole" -> "job"
  )


  setup("add-journey", "Add journey") withRequests(
    getPage("EORI number page", saveToken = true, s"$baseUrl/add-authority/eori-number"),
    postPage("EORI number Page", s"$baseUrl/add-authority/eori-number", s"$baseUrl/add-authority/accounts", "GB345834921000"),
    postPage("Accounts Page", s"$baseUrl/add-authority/accounts", s"$baseUrl/add-authority/eori-details-correct", Map("value[0]" -> "account_0")),
    postPage("EORI details correct page", s"$baseUrl/add-authority/eori-details-correct", s"$baseUrl/add-authority/start", "radioYes"),
    postPage("Start page", s"$baseUrl/add-authority/start", s"$baseUrl/add-authority/start-date", "setDate"),
    postPage("Start date Page", s"$baseUrl/add-authority/start-date", s"$baseUrl/add-authority/end", dateToMap(LocalDate.of(2028,10,10))),
    postPage("End page", s"$baseUrl/add-authority/end", s"$baseUrl/add-authority/end-date", "setDate"),
    postPage("End date page", s"$baseUrl/add-authority/end-date", s"$baseUrl/add-authority/available-balance", dateToMap(LocalDate.of(2028,11,10))),
    postPage("Available balance page", s"$baseUrl/add-authority/available-balance", s"$baseUrl/add-authority/your-details", "yes"),
    postPage("Your details page", s"$baseUrl/add-authority/your-details", s"$baseUrl/add-authority/check-answers", authorisedUserPayload),
    postPage("Check your answers page", s"$baseUrl/add-authority/check-answers", s"$baseUrl/add-authority/confirmation", "")
 )
}
