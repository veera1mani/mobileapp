package com.healthtraze.etraze.api.masters.service;

import java.io.ByteArrayInputStream; 
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.StringUtils;
import com.healthtraze.etraze.api.base.constant.Constants;
import com.healthtraze.etraze.api.base.constant.StringIteration;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.base.service.BaseService;
import com.healthtraze.etraze.api.base.util.CommonUtil;
import com.healthtraze.etraze.api.file.model.File;
import com.healthtraze.etraze.api.file.service.FileStorageService;
import com.healthtraze.etraze.api.masters.documents.Manufacturer;
import com.healthtraze.etraze.api.masters.dto.ItemsDto;
import com.healthtraze.etraze.api.masters.dto.ProductImportDTO;
import com.healthtraze.etraze.api.masters.model.Invoice;
import com.healthtraze.etraze.api.masters.model.InvoiceLineItems;
import com.healthtraze.etraze.api.masters.model.Items;
import com.healthtraze.etraze.api.masters.model.Product;
import com.healthtraze.etraze.api.masters.model.ProductComposite;
import com.healthtraze.etraze.api.masters.model.TempInvoice;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;
import com.healthtraze.etraze.api.masters.repository.InvoiceLineItemsRepository;
import com.healthtraze.etraze.api.masters.repository.InvoiceRepository;
import com.healthtraze.etraze.api.masters.repository.ItemsRepository;
import com.healthtraze.etraze.api.masters.repository.ListValueRepository;
import com.healthtraze.etraze.api.masters.repository.ManufacturerRepository;
import com.healthtraze.etraze.api.masters.repository.ProductRepository;
import com.healthtraze.etraze.api.masters.repository.TempInvoiceRepository;
import com.healthtraze.etraze.api.masters.repository.TenantManufactureRepository;
import com.healthtraze.etraze.api.masters.repository.TicketOrderInvoiceRepository;
import com.healthtraze.etraze.api.masters.repository.TicketOrderRepository;
import com.healthtraze.etraze.api.masters.repository.TicketRepository;
import com.healthtraze.etraze.api.security.model.User;
import com.healthtraze.etraze.api.security.repository.UserRepository;
import com.healthtraze.etraze.api.security.util.SecurityUtil;
import java.util.*;

@Service
public class ProductService implements BaseService<Product, String> {

	private Logger logger = LogManager.getLogger(ProductService.class);

	private final InvoiceRepository invoiceRepository;

	private final TicketOrderInvoiceRepository ticketOrderInvoiceRepository;

	private final ItemsRepository itemsRepository;

	private final ProductRepository productRepository;

	private final UserRepository userRepository;

	private final InvoiceLineItemsRepository invoiceLineItemsRepository;

	private final TicketService ticketService;

	private final FileStorageService fileStorageService;
	
	private final ListValueRepository listValueRepository;
	
	private final ManufacturerRepository manufacturerRepository;
	
	private final TenantManufactureRepository tenantManufactureRepository;
	
	private final TicketRepository ticketRepository;
	
	private final TempInvoiceRepository tempInvoiceRepository ;
	
	private final TicketOrderService ticketOrderService;

	

	@Override
	public List<Product> findAll() {
		try {
			return productRepository.findAll();
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
		}
		return Collections.emptyList();
	}

	
	@Autowired
	public ProductService(InvoiceRepository invoiceRepository,
			TicketOrderInvoiceRepository ticketOrderInvoiceRepository, ItemsRepository itemsRepository,
			ProductRepository productRepository, UserRepository userRepository,
			InvoiceLineItemsRepository invoiceLineItemsRepository, TicketService ticketService,
			FileStorageService fileStorageService, ListValueRepository listValueRepository,
			ManufacturerRepository manufacturerRepository, TenantManufactureRepository tenantManufactureRepository,
			TicketRepository ticketRepository, TempInvoiceRepository tempInvoiceRepository,
			TicketOrderService ticketOrderService) {
		super();
		this.invoiceRepository = invoiceRepository;
		this.ticketOrderInvoiceRepository = ticketOrderInvoiceRepository;
		this.itemsRepository = itemsRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.invoiceLineItemsRepository = invoiceLineItemsRepository;
		this.ticketService = ticketService;
		this.fileStorageService = fileStorageService;
		this.listValueRepository = listValueRepository;
		this.manufacturerRepository = manufacturerRepository;
		this.tenantManufactureRepository = tenantManufactureRepository;
		this.ticketRepository = ticketRepository;
		this.tempInvoiceRepository = tempInvoiceRepository;
		this.ticketOrderService = ticketOrderService;
	}



	@Override
	public Product findById(String id) {
		return null;
	}

	@Override
	public Result<Product> create(Product p) {
		Result<Product> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());

			if (user.isPresent()) {
				User u = user.get();
				ProductComposite key = new ProductComposite(p.getProductCode(),p.getManufacturer(),u.getTenantId());
				Optional<Product> option = productRepository.findById(key);
				if (option.isPresent()) {
					result.setCode("1111");
					result.setMessage("Product code already available");
					return result;
				}

				p.setCreatedOn(new Date());
				p.setCreatedBy(SecurityUtil.getUserName());
				p.setTenantId(user.get().getTenantId());
				Product product = productRepository.save(p);
				result.setCode("0000");
				result.setData(product);
				result.setMessage("Product addded sucessfully");
				return result;
			}
		} catch (Exception exc) {
			result.setCode("1111");
			result.setMessage("Product not added");
			logger.atError();

		}
		return result;
	}

	@Override
	public Result<Product> update(Product t) {
		Result<Product> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				ProductComposite key = new ProductComposite(t.getProductCode(), t.getManufacturer(), u.getTenantId());
				Optional<Product> option = productRepository.findById(key);
				if (option.isPresent()) {
					Product pr = option.get();
					if (t.getProductCode().equals(pr.getProductCode())) {
						pr.setProductCode(t.getProductCode());
						pr.setProductName(t.getProductName());
						pr.setManufacturer(t.getManufacturer());
						pr.setBin(t.getBin());
						pr.setWareHouseLocation(t.getWareHouseLocation());
						pr.setModifiedOn(new Date());
						pr.setModifiedBy(SecurityUtil.getUserName());
						pr.setRow(t.getRow());
						pr.setPallet(t.getPallet());
						Product prd = productRepository.save(pr);
						result.setCode(StringIteration.SUCCESS_CODE);
						result.setData(prd);
						result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
						return result;
					}
				} else {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("Code and Manufacturer cannot be changed");
					return result;
				}
			}
			
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	@Override
	public Result<Product> delete(String id) {
		return null;
	}

	public Result<Invoice> createInvoice(Invoice inv) {
		Result<Invoice> result = new Result<>();
		try {
			inv.setTenantId(inv.getTenantId());

			ZoneId zoneId = ZoneId.systemDefault();
			ZonedDateTime now = ZonedDateTime.now(zoneId);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMdd", Locale.ENGLISH);
			String strToday = dtf.format(now);

			Optional<Invoice> invoice = invoiceRepository.findById(inv.getInvoiceNumber());
			if (invoice.isPresent()) {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("Invoice Already Exists");
				return result;
			}

//			Optional<Manufacturer> man=manufacturerRepository.findByManufacturerNameAndStatus(inv.getManufacturerId().toLowerCase().replaceAll(" ", ""));
//			man.ifPresent(m->inv.setManufacturerId(m.getManufacturerId()));

			Optional<TempInvoice> temp = tempInvoiceRepository
					.findByInvoiceNumber(inv.getInvoiceNumber().toLowerCase().replace(" ", ""), inv.getTenantId());
			temp.ifPresent(m -> inv.setManufacturerId(m.getManufacturerId()));

			inv.setCreatedBy(SecurityUtil.getUserName());
			inv.setCreatedOn(new Date());
			for (Items it : inv.getItems()) {
				it.setManufacturerId(inv.getManufacturerId());
				it.setInvoiceId(inv.getInvoiceNumber());
				it.setId("IT" + strToday + "" + (itemsRepository.count() + 1));
				it.setTenantId(inv.getTenantId());
				itemsRepository.save(it);
			}
			ticketService.invoiceLineItemEntry(inv);

			Invoice iv = invoiceRepository.save(inv);
			tempInvoiceRepository.deleteById(temp.get().getId());
			result.setCode(StringIteration.SUCCESS_CODE);
			result.setMessage(StringIteration.CREATEDSUCCESSFULLY);
			result.setData(iv);
			return result;

		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage("Error While Create Invoice");
		}
		return result;
	}

	public Result<Invoice> createInvoiceManual(Invoice inv) {
		Result<Invoice> result = new Result<>();

		try {

			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				if (!u.getRoleName().equals(StringIteration.INVOICEUSER)) {
					result.setMessage("Invalid User");
					result.setCode(StringIteration.ERROR_CODE1);
					return result;
				}
				inv.setTenantId(u.getTenantId());

				ZoneId zoneId = ZoneId.systemDefault();
				ZonedDateTime now = ZonedDateTime.now(zoneId);
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMdd", Locale.ENGLISH);
				String strToday = dtf.format(now);

				Optional<Invoice> invoice = invoiceRepository.findById(inv.getInvoiceNumber());
				if (invoice.isPresent()) {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("Invoice Already Exists");
					return result;
				}

				CommonUtil.setCreatedOn(u);
				for (Items it : inv.getItems()) {
					it.setManufacturerId(u.getManufacturerId());
					it.setInvoiceId(inv.getInvoiceNumber());
					it.setId("IT" + strToday + "" + (itemsRepository.count() + 1));
					it.setTenantId(u.getTenantId());
					itemsRepository.save(it);
				}
				ticketService.invoiceLineItemEntry(inv.getInvoiceNumber());
				inv.setManufacturerId(u.getManufacturerId());
				Invoice iv = invoiceRepository.save(inv);
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.CREATEDSUCCESSFULLY);
				result.setData(iv);
				return result;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage("Error While Create Invoice");
		}
		return result;
	}

	public List<Map<String, Object>> getItemsByInvoiceNumber(List<String> invoices) {
		List<Map<String, Object>> result = new ArrayList<>();

		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				Optional<TicketOrderInvoice> inv = ticketOrderInvoiceRepository
						.findByInvoiceNumbeForPicklist(invoices.get(0), u.getTenantId());
				if (inv.isPresent()) {
					TicketOrderInvoice t = inv.get();
					if (Boolean.TRUE.equals(t.getIsSelected() && t.getSelectList() != null)
							&& t.getStatus().equals("INVOICE CREATED")) {
						invoices = ticketOrderInvoiceRepository.findInvoiceNumbetBySelectList(t.getSelectList(),
								u.getTenantId());
					}
				}

				Map<String, Map<String, Object>> itemTracker = new HashMap<>();

				for (String invoiceNumber : invoices) {

//					if(isFloorWise(u, invoiceNumber)) {  
//						List<InvoiceLineItems> invoiceLineItemsList = invoiceLineItemsRepository.findByInvoiceAndFloor(invoiceNumber,u.getWarehouseLocation());
//					}else{
//						List<InvoiceLineItems> invoiceLineItemsList = invoiceLineItemsRepository
//							.findByInvoiceNumber(invoiceNumber);
//					}	

					List<InvoiceLineItems> invoiceLineItemsList = invoiceLineItemsRepository
							.findByInvoiceAndFloor(invoiceNumber, u.getWarehouseLocation());

					for (InvoiceLineItems invoiceLineItem : invoiceLineItemsList) {
						String productCode = invoiceLineItem.getProductCode();

						Optional<Invoice> invs = invoiceRepository.findById(invoiceNumber);

						if (!invs.isPresent()) {
							logger.info("invoice not found {} number", invs);
							return Collections.emptyList();
						}
						Product product = productRepository.getProductByCode(productCode, u.getTenantId(),
								invs.get().getManufacturerId());
						if (product != null) {
							String key = invoiceLineItem.getBatchNumber() + "-" + product.getWareHouseLocation();

							if (itemTracker.containsKey(key)) {
								Map<String, Object> existingItem = itemTracker.get(key);
								int currentQuantity = Integer
										.parseInt(existingItem.get(StringIteration.QUANTITY).toString());
								int additionalQuantity = Integer.parseInt(invoiceLineItem.getQuantity());
								existingItem.put("quantity", String.valueOf(currentQuantity+ additionalQuantity));
								String inString = invoiceLineItem.getId();
								String vaString = String.valueOf(existingItem.get("id"));
								String val = inString.concat(",").concat(vaString);
								existingItem.put("groupId", val);

							} else {
								Map<String, Object> itemDetails = new HashMap<>();
								itemDetails.put("id", invoiceLineItem.getId());
								itemDetails.put("groupId", invoiceLineItem.getId());
								itemDetails.put("invoiceId", invoiceLineItem.getInvoiceId());
								itemDetails.put("productCode", productCode);
								itemDetails.put("productName", invoiceLineItem.getProductName());
								itemDetails.put("quantity", invoiceLineItem.getQuantity());
								itemDetails.put("batchNumber", invoiceLineItem.getBatchNumber());
								itemDetails.put("expiryDate", invoiceLineItem.getExpiryDate());
								itemDetails.put("mrp", invoiceLineItem.getMrp());
								itemDetails.put("pickItems", invoiceLineItem.getPickItems());
								itemDetails.put("checked", invoiceLineItem.isChecked());
								itemDetails.put("picked", invoiceLineItem.isPicked());
								itemDetails.put("bin", product.getBin());

								if (product.getWareHouseLocation() != null) {
									List<Object[]> resultList = listValueRepository.findNameAndValueByCodeAndKey(
											Constants.WARE_HOUSE_LOCATION, product.getWareHouseLocation());
									if (!resultList.isEmpty()) {
										Object[] res = resultList.get(0);
										String name = (String) res[0];
										String value = (String) res[1];

										itemDetails.put("location", name);
										itemDetails.put("value", value);
									}
								}

								itemDetails.put("partialPick", invoiceLineItem.isPartialPick());
								itemDetails.put("partialCheck", invoiceLineItem.isPartialCheck());

								itemTracker.put(key, itemDetails);
							}
						} else {
							String key = invoiceLineItem.getBatchNumber() + "-" + invoiceLineItem.getId();
							Map<String, Object> itemDetails = new HashMap<>();
							itemDetails.put("id", invoiceLineItem.getId());
							itemDetails.put("invoiceId", invoiceLineItem.getInvoiceId());
							itemDetails.put("productCode", productCode);
							itemDetails.put("productName", invoiceLineItem.getProductName());
							itemDetails.put("quantity", invoiceLineItem.getQuantity());
							itemDetails.put("batchNumber", invoiceLineItem.getBatchNumber());
							itemDetails.put("expiryDate", invoiceLineItem.getExpiryDate());
							itemDetails.put("mrp", invoiceLineItem.getMrp());
							itemDetails.put("pickItems", invoiceLineItem.getPickItems());
							itemDetails.put("checked", invoiceLineItem.isChecked());
							itemDetails.put("picked", invoiceLineItem.isPicked());
							itemDetails.put("bin", null);
							itemDetails.put("location", "unknown");
							itemDetails.put("value", null);
							itemDetails.put("partialPick", invoiceLineItem.isPartialPick());
							itemDetails.put("partialCheck", invoiceLineItem.isPartialCheck());
							itemTracker.put(key, itemDetails);
						}
					}
				}

				result.addAll(itemTracker.values());
				// Set<String> list = getLocations(itemTracker.values());

				// System.err.println("set :: "+ list);

				Collections.sort(result, new Comparator<Map<String, Object>>() {
					@Override
					public int compare(Map<String, Object> o1, Map<String, Object> o2) {
						String value1 = (String) o1.get("value");
						String value2 = (String) o2.get("value");
						String bin1 = (String) o1.get("bin");
						String bin2 = (String) o2.get("bin");
						int locationCompare;
						if (value1 == null && value2 == null) {
							locationCompare = 0;
						} else if (value1 == null) {
							locationCompare = 1;
						} else if (value2 == null) {
							locationCompare = -1;
						} else {
							locationCompare = value1.compareTo(value2);
						}
						if (locationCompare != 0) {
							return locationCompare;
						}
						if (bin1 == null && bin2 == null)
							return 0;
						if (bin1 == null)
							return 1;
						if (bin2 == null)
							return -1;
						return bin1.compareTo(bin2);
					}
				});
			}

		} catch (Exception e) {
			logger.error("Error retrieving items by invoice number", e);
		}

		return result;
	}

	private Boolean isFloorWise(User u, String inv) {
		try {
			Optional<TenantManufacture> optional = tenantManufactureRepository.findByInvoiceNumber(inv,
					u.getTenantId());
			if (optional.isPresent()) {
				TenantManufacture tm = optional.get();
				System.err.println("tm is floor wise " + tm.isFloorWise());
				return tm.isFloorWise();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	private Set<String> getLocations(Collection<Map<String, Object>> maps) {
		Set<String> set = new HashSet<>();
		try {
			for (Map<String, Object> map : maps) {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if (entry.getKey().equals("location")) {
						set.add((String) entry.getValue());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return set;
	}

	public Result<InvoiceLineItems> updateInvoiceLineItems(InvoiceLineItems t) {
		Result<InvoiceLineItems> result = new Result<>();
		try {
			Optional<InvoiceLineItems> option = invoiceLineItemsRepository.findById(t.getId());
			if (option.isPresent()) {
				InvoiceLineItems ilt = option.get();
				if (ilt.getId().equals(t.getId())) {
					ilt.setProductCode(t.getProductCode());
					ilt.setInvoiceId(t.getInvoiceId());
					ilt.setStatus(t.getStatus());
					ilt.setChecked(t.isChecked());
					ilt.setPicked(t.isPicked());
					ilt.setPickItems(t.getPickItems());
					InvoiceLineItems in = invoiceLineItemsRepository.save(ilt);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
					result.setData(in);
					return result;
				}
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("");
				return result;
			}
		} catch (Exception e) {
			logger.error(StringIteration.SPACE, e);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	private void setProductCode(ProductImportDTO dto) {
		try {
			Cell cell = dto.getRow().getCell(0);
			if (cell == null || cell.getCellType() == CellType.BLANK) {
				dto.setValid(false);
				dto.setMessage("product Code Is Empty");
			} else {
				if (cell.getCellType() == CellType.NUMERIC) {
					double numericValue = cell.getNumericCellValue();
					long longValue = (long) numericValue;
					dto.getProduct().setProductCode(String.valueOf(longValue));
				} else if (cell.getCellType() == CellType.STRING) {
					String stringValue = cell.getStringCellValue();
					dto.getProduct().setProductCode(stringValue);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void setProductName(ProductImportDTO dto) {
		try {
			Cell cell = dto.getRow().getCell(1);
			if (cell == null || cell.getCellType() == CellType.BLANK) {
				dto.setValid(false);
				dto.setMessage("product Name Is Empty");
			} else {
				if (cell.getCellType() == CellType.NUMERIC) {
					double numericValue = cell.getNumericCellValue();
					long longValue = (long) numericValue;
					dto.getProduct().setProductName(String.valueOf(longValue));
				} else if (cell.getCellType() == CellType.STRING) {
					String stringValue = cell.getStringCellValue();
					dto.getProduct().setProductName(stringValue);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void setManufacturer(ProductImportDTO dto, User u) {
		try {
			Cell cell = dto.getRow().getCell(2);
			if (cell == null || cell.getCellType() == CellType.BLANK) {
				dto.setValid(false);
				dto.setMessage(StringIteration.MANUFACTURERISEMPTY);
			} else {
				if (cell.getCellType() == CellType.NUMERIC) {
					double numericValue = cell.getNumericCellValue();
					long longValue = (long) numericValue;
					dto.getProduct().setManufacturer(String.valueOf(longValue));
					dto.getProduct().setManufacturerName(String.valueOf(longValue));
				} else if (cell.getCellType() == CellType.STRING) {
					String stringValue = cell.getStringCellValue();
					dto.getProduct().setManufacturerName(stringValue);
					if (stringValue != null) {
						Optional<Manufacturer> manf = manufacturerRepository
								.findByManufacturerNameAndStatus(stringValue.replace(" ", "").toLowerCase());
						if (manf.isPresent()) {
							Optional<TenantManufacture> tm = tenantManufactureRepository
									.findByManufactureIdAndTenantAndStatus(manf.get().getManufacturerId(),
											u.getTenantId());
							if (tm.isPresent()) {
								dto.getProduct().setManufacturer(manf.get().getManufacturerId());
							} else {
								dto.setValid(false);
								dto.setMessage("this manufacturer is not active for this tenant , ");
							}
						} else {
							dto.setValid(false);
							dto.setMessage("manufacturer not found , ");
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void setWareHouseLocation(ProductImportDTO dto) {
		try {
			Cell cell = dto.getRow().getCell(3);
			if (cell == null || cell.getCellType() == CellType.BLANK) {
				dto.setValid(false);
				dto.setMessage("Warehouse location is empty");
			} else {
				if (cell.getCellType() == CellType.NUMERIC) {
					double numericValue = cell.getNumericCellValue();
					long longValue = (long) numericValue;
					dto.getProduct().setWareHouseLocation(String.valueOf(longValue));
				} else if (cell.getCellType() == CellType.STRING) {
					String stringValue = cell.getStringCellValue();
					dto.getProduct().setWareHouseLocation(stringValue);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void setBin(ProductImportDTO dto) {
		try {
			Cell cell = dto.getRow().getCell(4);
			if (cell == null || cell.getCellType() == CellType.BLANK) {
				dto.setValid(false);
				dto.setMessage("Bin Is Empty");
			} else {
				if (cell.getCellType() == CellType.NUMERIC) {
					double numericValue = cell.getNumericCellValue();
					long longValue = (long) numericValue;
					dto.getProduct().setBin(String.valueOf(longValue));
				} else if (cell.getCellType() == CellType.STRING) {
					String stringValue = cell.getStringCellValue();
					dto.getProduct().setBin(stringValue);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void prepareProduct(ProductImportDTO dto, User u) {
		setProductCode(dto);
		setProductName(dto);
		setManufacturer(dto, u);
		setWareHouseLocation(dto);
		setBin(dto);
	}

	public void validation(ProductImportDTO dto, User u) {
		if (dto.getProduct().getManufacturerName() != null) {
			Optional<Manufacturer> manf = manufacturerRepository.findByManufacturerNameAndStatus(
					dto.getProduct().getManufacturerName().replace(" ", "").toLowerCase());
			if (manf.isPresent()) {
				Optional<TenantManufacture> tm = tenantManufactureRepository
						.findByManufactureIdAndTenantAndStatus(manf.get().getManufacturerId(), u.getTenantId());
				if (tm.isPresent()) {
					dto.getProduct().setManufacturer(manf.get().getManufacturerId());
					Optional<String> code = listValueRepository.findByNameAndCode("110",
							dto.getProduct().getWareHouseLocation().toLowerCase().replaceAll(" ", ""));
					if (code.isPresent()) {
						dto.getProduct().setWareHouseLocation(code.get());
					} else {
						dto.setValid(false);
						dto.setMessage("warehouse location not found , ");
					}
				} else {
					dto.setValid(false);
					dto.setMessage("this manufacturer is not active for this tenant , ");
				}
			} else {
				dto.setValid(false);
				dto.setMessage("manufacturer not found , ");
			}
		}
	}

	public Result<Object> fileReader(MultipartFile file, String sheetIndex) {
		List<Product> products = new ArrayList<>();
		List<ProductImportDTO> invalidProducts = new ArrayList<>();
		Result<Object> result = new Result<>();
		try (FileInputStream fileInputStream = new FileInputStream(CommonUtil.convertMultiPartToFile(file));
				XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);) {

			Optional<User> optional = userRepository.findById(SecurityUtil.getUserName());
			if (optional.isPresent()) {
				User us = optional.get();
				XSSFSheet sheet = workbook.getSheetAt(Integer.parseInt(sheetIndex));

				int rowIndex = 0;
				for (Row row : sheet) {
					if (rowIndex == 0) {
						rowIndex++;
						continue;
					}
					Product product = new Product();
					ProductImportDTO dto = new ProductImportDTO(product, true, row);
					prepareProduct(dto, us);
					validation(dto, us);
					if (dto.isValid()) {
						product.setCreatedOn(new Date());
						product.setCreatedBy(us.getUserId());
						setpalletAndRow(product);
						product.setTenantId(us.getTenantId());
						products.add(product);
					} else {
//						product.setRemarks(dto.getMessage());
						invalidProducts.add(dto);
					}

					rowIndex++;
				}

				createProduct(products, invalidProducts);
				result.setCode(StringIteration.SUCCESS_CODE);

				return !invalidProducts.isEmpty() ? invalidProduct(result, invalidProducts) : result;

			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return result;
	}

	private Result<Object> invalidProduct(Result<Object> result, List<ProductImportDTO> invalidProducts) {
		try (XSSFWorkbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {

			String[] headerList = { "Product Code", "Product Name", "Manufacturer", "WareHouseLocation", "Bin",
					"Remarks" };
			XSSFSheet spreadsheet = workbook.createSheet(" invalid product Excel ");

			int rowId = 0;
			XSSFRow row1;
			row1 = spreadsheet.createRow(rowId++);
			int r = 0;
			for (String s : headerList) {
				row1.createCell(r).setCellValue(s);
				r++;
			}
			for (ProductImportDTO i : invalidProducts) {

				Product p = i.getProduct();

				row1 = spreadsheet.createRow(rowId++);
				row1.createCell(0).setCellValue(p.getProductCode());
				row1.createCell(1).setCellValue(p.getProductName());
				row1.createCell(2).setCellValue(p.getManufacturerName());
				row1.createCell(3).setCellValue(p.getWareHouseLocation());
				row1.createCell(4).setCellValue(p.getBin());
				row1.createCell(5).setCellValue(i.getMessage());
			}
			workbook.write(byteArrayOutputStream);
			InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			String fileId = UUID.randomUUID().toString();
			File invalidProduct = fileStorageService.uploadFileToAWS(fileId, fileId, byteArrayInputStream,
					"invalidProduct.xlsx", "document", 0);
			result.setCode(StringIteration.ERROR_CODE1);
			result.setData(invalidProduct);
			result.setMessage("invalid data found");
			byteArrayOutputStream.flush();
			return result;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return result;
	}

	public void createProduct(List<Product> products, List<ProductImportDTO> invalidProducts) {
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				if (!products.isEmpty()) {
					products.forEach(p -> {
						ProductComposite key = new ProductComposite(p.getProductCode(), p.getManufacturer(),
								u.getTenantId());
						Optional<Product> pro = productRepository.findById(key);
						if (pro.isPresent()) {
							ProductImportDTO importDTO = new ProductImportDTO(p, "Product Code Already Exist");
							invalidProducts.add(importDTO);
						} else {
							productRepository.save(p);
						}
					});
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void setpalletAndRow(Product product) {
		if (!product.getBin().equals(null) && product.getBin().contains("-")) {
			String[] bin = product.getBin().split("-");
			product.setRow(bin[0]);
			product.setPallet(bin[1]);
		}
	}

	public Result<Page<Product>> findAllProduct(int page, String value, String sortBy, String sortDir) {
		Result<Page<Product>> result = new Result<>();
		try {
			Optional<User> user = userRepository.findByUserId(SecurityUtil.getUserName());
			if (user.isPresent()) {
				Pageable paging = createPageableProduct(page, sortBy, sortDir);
				Page<Product> product = productRepository.findByTenantId(paging, user.get().getTenantId(), value);

				for (Product p : product.getContent()) {
					if (p.getWareHouseLocation() != null) {
						listValueRepository.findByCode(Constants.WARE_HOUSE_LOCATION, p.getWareHouseLocation())
								.ifPresent(p::setWareHouseLocationName);

					}

					if (p.getManufacturer() != null) {
						manufacturerRepository.findById(p.getManufacturer())
								.ifPresent(t -> p.setManufacturerName(t.getManufacturerName()));
					}

				}

				result.setData(product);
				result.setCode("0000");
				result.setMessage("success");

			}
		} catch (Exception ex) {
			logger.error(ex);
			result.setCode("1111");

		}
		return result;
	}

	private Pageable createPageableProduct(int page, String sortBy, String sortDir) {
		if (StringUtils.isNullOrEmpty(sortBy)) {
			sortBy = "bin";
		}
		if (StringUtils.isNullOrEmpty(sortDir)) {
			sortDir = "ASC";
		}
		return PageRequest.of(page, Constants.PAGE_SIZE_10, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
	}

	public Map<String, List<Items>> invoiceItems(String search) {
		Optional<User> userOptional = userRepository.findById(SecurityUtil.getUserName());
		if (userOptional.isPresent()) {
			User us = userOptional.get();

			if (us.getRoleName().equals(StringIteration.SUPERADMIN)) {
				return invoiceSuperAdmin(search, us);
			} else if (us.getRoleName().equals(StringIteration.INVOICEUSER)) {
				return invoiceUser(search, us);
			}
		}
		return Collections.emptyMap();
	}

	private Map<String, List<Items>> invoiceUser(String search, User us) {
		List<Items> items = itemsRepository.findItemsByInvoiceNumberForInvoiceUser(us.getTenantId(),
				us.getManufacturerId(), search);
		return items.stream()
				.collect(Collectors.groupingBy(Items::getInvoiceId, LinkedHashMap::new, Collectors.toList()));
	}

	private Map<String, List<Items>> invoiceSuperAdmin(String search, User us) {
		List<Items> items = itemsRepository.findItemsByInvoiceNumber(us.getTenantId(), search);
		return items.stream()
				.collect(Collectors.groupingBy(Items::getInvoiceId, LinkedHashMap::new, Collectors.toList()));
	}

	public Result<InvoiceLineItems> updateQuantity(InvoiceLineItems i) {
		Result<InvoiceLineItems> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				Optional<TicketOrderInvoice> toi = ticketOrderInvoiceRepository
						.findByInvoiceAndTenantId(i.getInvoiceId(), u.getTenantId());
				if (toi.isPresent()) {
					TicketOrderInvoice t = toi.get();
					if (!t.getStatus().equals(StringIteration.PICKED)) {
						result.setMessage("This invoice is not picked yet");
						result.setCode(StringIteration.ERROR_CODE1);
						return result;
					}
				}
				Optional<InvoiceLineItems> optional = invoiceLineItemsRepository.findByInvoiceAndCode(i.getInvoiceId(),
						i.getProductCode(), u.getTenantId());
				if (optional.isPresent()) {
					InvoiceLineItems inv = optional.get();
					inv.setPickItems(i.getPickItems());
					invoiceLineItemsRepository.save(inv);
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
					result.setData(inv);
				} else {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("invalid invoice or product code");
				}
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("invalid user");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
		}
		return result;
	}

	public Result<List<ItemsDto>> updateQuantityMultiple(InvoiceLineItems inv) {
		Result<List<ItemsDto>> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				List<InvoiceLineItems> invoiceLineItemsList = invoiceLineItemsRepository
						.findByInvoiceNumber(inv.getInvoiceId());
				if (invoiceLineItemsList != null) {
					for (InvoiceLineItems invoiceLineItem : invoiceLineItemsList) {
						for (ItemsDto item : inv.getItems()) {
							if (invoiceLineItem.getProductCode().equals(item.getProductCode())) {
								invoiceLineItem.setPickItems(item.getPickItems());
								item.setQuantity(invoiceLineItem.getQuantity());
							}
						}
					}

					invoiceLineItemsRepository.saveAll(invoiceLineItemsList);
					result.setData(inv.getItems());
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage("Pick items updated successfully.");
				} else {
					result.setCode(StringIteration.ERROR_CODE1);
					result.setMessage("No invoice line items found for the invoice number.");
				}
			} else {
				result.setCode(StringIteration.ERROR_CODE1);
				result.setMessage("Invalid user");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setCode(StringIteration.ERROR_CODE1);
			result.setMessage("An error occurred while updating pick items.");
		}
		return result;
	}

	public Result<List<InvoiceLineItems>> updatePartialPickItems(String status, List<InvoiceLineItems> li) {
		Result<List<InvoiceLineItems>> result = new Result<>();
		List<InvoiceLineItems> list = new ArrayList<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
					
				li.forEach( i ->{
					String[] liStrings = i.getGroupId().split(",");
					for (String ii : liStrings) {
						Optional<InvoiceLineItems> optional = invoiceLineItemsRepository.findById(ii);
						optional.ifPresent(jk -> {
							if (status.equals(StringIteration.PICKED)) {
								jk.setPartialPick(true);
							} else if (status.equals(StringIteration.CHECKEDD)) {
								jk.setPartialCheck(true);
							}
							invoiceLineItemsRepository.save(jk);
						});
					}
				
				});
				
				
				Optional<TicketOrderInvoice> opt = ticketOrderInvoiceRepository
						.findByInvoiceAndTenantId(li.get(0).getInvoiceId(), u.getTenantId());
				if (opt.isPresent()) {
					TicketOrderInvoice toi = opt.get();
					List<InvoiceLineItems> list1 = invoiceLineItemsRepository
							.findByInvoiceNumberForPick(toi.getInvoiceNumber());
					if (list1.isEmpty() && toi.getStatus().equals(StringIteration.INVOICE_CREATED)) {
						toi.setStatus(StringIteration.PICKED);
						ticketOrderService.updateTicketOrderInvoice(toi);
					}
					List<InvoiceLineItems> list2 = invoiceLineItemsRepository
							.findByInvoiceNumberForCheck(toi.getInvoiceNumber());
					if (list2.isEmpty() && toi.getStatus().equals(StringIteration.PICKED)) {
						toi.setStatus(StringIteration.CHECKEDD);
						ticketOrderService.updateTicketOrderInvoice(toi);
					}
				}
				result.setCode(StringIteration.SUCCESS_CODE);
				result.setMessage(StringIteration.UPDATEDSUCCESSFULLY);
				result.setData(list);
				return result;
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}
		return result;
	}

	// update tbl_invoice_line_items set partial_pick = 'false' where invoice_id =
	// 'OS33125120'

	public Result<String> allPicked(String inv) {
		Result<String> result = new Result<>();
		try {
			Optional<User> us = userRepository.findById(SecurityUtil.getUserName());
			if (us.isPresent()) {
				User u = us.get();
				List<InvoiceLineItems> list = invoiceLineItemsRepository.findByInvoiceAndFloorByUser(inv,
						u.getWarehouseLocation());
				if (list.isEmpty()) {
					result.setCode(StringIteration.SUCCESS_CODE2);
					result.setMessage("Ready to Check");
				} else {
					result.setCode(StringIteration.SUCCESS_CODE);
					result.setMessage("Ready to Pick");
				}
			}
		} catch (Exception e) {
			result.setCode(StringIteration.ERROR_CODE1);
			logger.error(e.getMessage());
		}
		return result;
	}

}
