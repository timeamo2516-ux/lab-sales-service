package com.techstore.sales;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record SaleRequest(@NotNull Long productId,@Min(1) int quantity){}
