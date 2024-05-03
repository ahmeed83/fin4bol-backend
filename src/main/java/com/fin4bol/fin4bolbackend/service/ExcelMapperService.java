package com.fin4bol.fin4bolbackend.service;

import com.fin4bol.fin4bolbackend.repository.ProductRepository;
import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import com.fin4bol.fin4bolbackend.repository.entiry.Performance;
import com.fin4bol.fin4bolbackend.repository.entiry.PerformanceRapport;
import com.fin4bol.fin4bolbackend.repository.entiry.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

@Service
public class ExcelMapperService {

    private static final String SALES_DESCRIPTION = "Verkoopprijs artikel(en), ontvangen van kopers en door bol.com door te storten";
    private static final String COMMISSION_DESCRIPTION = "Commissie";
    private static final String COMMISSION_CORRECTION_DESCRIPTION = "Correctie commissie";
    private static final String LOST_ITEM_COMPENSATION_DESCRIPTION = "Compensatie zoekgeraakte artikel(en)";
    private static final String SHIPPING_COST_DESCRIPTION = "Verzendkosten";
    private static final String SHIPPING_COST_CORRECTION_DESCRIPTION = "Correctie verzendkosten";
    private static final String SHIPPING_VIA_BOL_DESCRIPTION = "Verzenden via bol.com - verzendzegel";
    private static final String UNSELLABLE_INVENTORY_COSTS_DESCRIPTION = "Onverkoopbare voorraadkosten";
    private static final String PICK_AND_PACK_COSTS_DESCRIPTION = "Pick&pack kosten";
    private static final String PICK_AND_PACK_CORRECTION_COSTS_DESCRIPTION = "Correctie pick&pack kosten";
    private static final String INVENTORY_COSTS_DESCRIPTION = "Voorraadkosten";
    private static final String ITEMS_SALES_PRICE_CORRECTION_DESCRIPTION = "Correctie verkoopprijs artikel(en)";

    private final ProductRepository productRepository;

    public ExcelMapperService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PerformanceRapport addPerformanceData(final ApplicationUser applicationUser,
                                                 final MultipartFile specification) {
        try (Workbook workbook = WorkbookFactory.create(specification.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            PerformanceRapport performanceRapport = new PerformanceRapport();
            setMainValues(sheet, performanceRapport);
            setPerformanceList(applicationUser, sheet, performanceRapport);
            return performanceRapport;
        } catch (Exception e) {
            throw new RuntimeException(format("Failed to parse and analyze the Excel file %s", e.getMessage()));
        }
    }

    private void setPerformanceList(final ApplicationUser applicationUser,
                                    final Sheet sheet,
                                    final PerformanceRapport performanceRapport) {
        Set<String> existingEanList = new HashSet<>();
        for (Row row : sheet) {
            Cell cellB = row.getCell(1); // EAN txt
            if (cellB != null && "EAN".equals(cellB.getStringCellValue())) {
                Cell cellC = row.getCell(2); // EAN Value
                if (cellC != null) {
                    final String ean = cellC.getStringCellValue();
                    if (existingEanList.add(ean)) {
                        final Product product =
                                productRepository.findByApplicationUserIdAndEanNumberOrderByUpdatedAtDesc(applicationUser, ean)
                                        .orElseGet(() -> createNewProduct(applicationUser, ean, row));
                        performanceRapport
                                .getPerformanceList()
                                .add(buildPerformance(sheet, product, ean, performanceRapport));
                    }
                }
            }
        }
    }

    private Product createNewProduct(final ApplicationUser applicationUser,
                                     final String ean,
                                     final Row row) {
        final Product product = new Product();
        product.setPurchaseCost(0.0);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setApplicationUserId(applicationUser);

        Cell cellD = row.getCell(3); // Product Name
        if (cellD != null && cellD.getCellType() == CellType.STRING) {
            product.setName(cellD.getStringCellValue());
            product.setEanNumber(ean);
        } else {
            product.setName("Not found");
            product.setEanNumber("Not found");
        }
        productRepository.save(product);
        return product;
    }

    private Performance buildPerformance(final Sheet sheet,
                                         final Product product,
                                         final String chosenEan,
                                         final PerformanceRapport performanceRapport) {
        Performance performance = new Performance();
        performance.setPerformanceRapport(performanceRapport);
        final LocalDateTime now = LocalDateTime.now();
        performance.setCreatedAt(now);
        performance.setUpdatedAt(now);
        performance.setName(product.getName());
        performance.setEanNumber(chosenEan);
        performance.setPurchaseCost(product.getPurchaseCost());
        final double totalRevenue = calculateTotalRevenuePerType(sheet, chosenEan, SALES_DESCRIPTION);
        performance.setRevenue(totalRevenue);
        double countedSoldEans = countEan(sheet, chosenEan);
        performance.setSalesVolume((int) countedSoldEans);
        final double averagePricePerProduct = calculateAveragePricePerProduct(countedSoldEans, totalRevenue);
        performance.setAveragePricePerProduct(averagePricePerProduct);
        performance.setVat(calculateVat(countedSoldEans, averagePricePerProduct));
        final double commission = calculateTotalRevenuePerType(sheet, chosenEan, COMMISSION_DESCRIPTION) * -1;
        performance.setCommission(commission);
        final double commissionCorrection = calculateTotalRevenuePerType(sheet, chosenEan, COMMISSION_CORRECTION_DESCRIPTION);
        performance.setCommissionCorrection(commissionCorrection);
        final double lostItemCompensation = calculateTotalRevenuePerType(sheet, chosenEan, LOST_ITEM_COMPENSATION_DESCRIPTION);
        performance.setLostItemCompensation(lostItemCompensation);
        performance.setNetCommission(commission - (commissionCorrection + lostItemCompensation));
        final Set<String> ordersByEan = calculateOrdersByEan(sheet, chosenEan);
        final Double shippingCost = calculateShipping(sheet, ordersByEan, SHIPPING_COST_DESCRIPTION);
        performance.setShippingCost(shippingCost);
        final Double shippingCostCorrection = calculateShipping(sheet, ordersByEan, SHIPPING_COST_CORRECTION_DESCRIPTION);
        performance.setShippingCostCorrection(shippingCostCorrection);
        final Double bolComShippingLabelCost = calculateShipping(sheet, ordersByEan, SHIPPING_VIA_BOL_DESCRIPTION);
        performance.setBolComShippingLabelCost(bolComShippingLabelCost);
        performance.setTotalShippingCost(shippingCost - (shippingCostCorrection + bolComShippingLabelCost) * -1);
        final double unsellableInventoryCost = calculateTotalRevenuePerType(sheet, chosenEan, UNSELLABLE_INVENTORY_COSTS_DESCRIPTION) * -1;
        performance.setUnsellableInventoryCost(unsellableInventoryCost);
        final double pickPackCost = calculateTotalRevenuePerType(sheet, chosenEan, PICK_AND_PACK_COSTS_DESCRIPTION) * -1;
        performance.setPickPackCost(pickPackCost);
        final double pickPackCostCorrection = calculateTotalRevenuePerType(sheet, chosenEan, PICK_AND_PACK_CORRECTION_COSTS_DESCRIPTION);
        performance.setPickPackCostCorrection(pickPackCostCorrection);
        performance.setNetPickPackCost(unsellableInventoryCost - (pickPackCost + pickPackCostCorrection) * -1);
        performance.setInventoryCost(calculateTotalRevenuePerType(sheet, chosenEan, INVENTORY_COSTS_DESCRIPTION) * -1);
        final double salesPriceCorrection = calculateTotalRevenuePerType(sheet, chosenEan, ITEMS_SALES_PRICE_CORRECTION_DESCRIPTION) * -1;
        performance.setSalesPriceCorrection(salesPriceCorrection);
        performance.setSalesPriceCorrectionVat(calculateVat(countedSoldEans, salesPriceCorrection));
        return performance;
    }

    private Double calculateShipping(final Sheet sheet,
                                     final Set<String> orderByEan,
                                     final String shippingTypeDescription) {
        double totalShippingCost = 0;
        for (Row row : sheet) {
            Cell cellA = row.getCell(0); // Description
            Cell cellF = row.getCell(5); // Order Number
            Cell cellJ = row.getCell(9); // Amount (Revenue)

            if (cellA != null && cellF != null && cellJ != null
                    && shippingTypeDescription.equals(cellA.getStringCellValue())
                    && orderByEan.contains(cellF.getStringCellValue())
                    && cellJ.getCellType() == CellType.NUMERIC) {
                totalShippingCost += cellJ.getNumericCellValue();
            }
        }
        return totalShippingCost;
    }

    private Set<String> calculateOrdersByEan(final Sheet sheet, final String chosenEan) {
        Set<String> orderNumbers = new HashSet<>();

        for (Row row : sheet) {
            Cell cellA = row.getCell(0); // Description
            Cell cellC = row.getCell(2); // EAN
            Cell cellF = row.getCell(5); // Order Number
            if (cellA != null && cellC != null && cellF != null
                    && chosenEan.equals(cellC.getStringCellValue())
                    && COMMISSION_DESCRIPTION.equals(cellA.getStringCellValue())) {
                orderNumbers.add(cellF.getStringCellValue());
            }
        }
        return orderNumbers;
    }


    private Double calculateVat(final double countedSoldEans, final double averagePricePerProduct) {
        return countedSoldEans * (averagePricePerProduct / 121 * 21);
    }

    private double calculateAveragePricePerProduct(final double countedSoldEans,
                                                   final double totalRevenue) {
        return countedSoldEans > 0 ? totalRevenue / countedSoldEans : 0;
    }

    private double calculateTotalRevenuePerType(final Sheet sheet,
                                                final String chosenEan,
                                                final String description) {
        double totalRevenue = 0;
        for (Row row : sheet) {
            Cell cellA = row.getCell(0); // Description
            Cell cellC = row.getCell(2); // EAN
            Cell cellJ = row.getCell(9); // Bedrag (Revenue)
            if (cellA != null && description.equals(cellA.getStringCellValue()) &&
                    cellC != null && chosenEan.equals(cellC.getStringCellValue()) &&
                    cellJ != null && cellJ.getCellType() == CellType.NUMERIC) {
                totalRevenue += cellJ.getNumericCellValue() * -1;
            }
        }
        return totalRevenue;
    }

    private double countEan(final Sheet sheet, final String chosenEan) {
        double count = 0;
        for (Row row : sheet) {
            Cell cellA = row.getCell(0); // SALES_DESCRIPTION
            Cell cellC = row.getCell(2); // EAN
            if (cellA != null && SALES_DESCRIPTION.equals(cellA.getStringCellValue()) &&
                    cellC != null && chosenEan.equals(cellC.getStringCellValue())) {
                count++;
            }
        }
        return count;
    }

    private void setMainValues(final Sheet sheet,
                               final PerformanceRapport performanceRapport) {
        boolean handlePeriod = false;
        boolean salespersonSet = false;
        for (Row row : sheet) {
            Cell cellA = row.getCell(0); // Periode txt
            if (cellA != null) {
                Cell cellB = row.getCell(1); // Periode Value
                if ("Periode:".equals(cellA.getStringCellValue())) {
                    handlePeriod = isPeriodSet(performanceRapport, cellB, handlePeriod);
                } else if ("Verkopernummer:".equals(cellA.getStringCellValue())) {
                    salespersonSet = handleSalesPerson(performanceRapport, cellB, salespersonSet);
                }
            }
            if (handlePeriod && salespersonSet) break;
        }
    }

    private boolean handleSalesPerson(final PerformanceRapport performanceRapport,
                                      final Cell salespersonNumber,
                                      boolean salespersonSet) {
        if (salespersonNumber != null) {
            performanceRapport.setSalespersonNumber(salespersonNumber.getStringCellValue());
            salespersonSet = true;
        }
        return salespersonSet;
    }

    private boolean isPeriodSet(final PerformanceRapport performanceRapport,
                                final Cell period,
                                boolean periodSet) {
        if (period != null) {
            performanceRapport.setPeriod(period.getStringCellValue());
            periodSet = true;
        }
        return periodSet;
    }
}
