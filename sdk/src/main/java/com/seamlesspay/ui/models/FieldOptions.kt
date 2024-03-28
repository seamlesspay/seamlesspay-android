package com.seamlesspay.ui.models

data class FieldOptions(
  val cvv: FieldConfiguration,
  val postalCode: FieldConfiguration
)

data class FieldConfiguration(
  val display: Boolean = true,
  val required: Boolean = true,
)