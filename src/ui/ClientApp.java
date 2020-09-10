/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import database.Manufacturers;
import database.Toys;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import models.Manufacturer;
import models.Message;
import models.ThankYouMessage;
import models.Toy;
import protocols.ClientProtocol;
import sockets.SocketClient;
import utils.CountryUtil;
import utils.FormUtil;
import utils.RandomUtil;
import utils.TimeUtil;

/**
 *
 * @author Jerry Auvagha
 */
public class ClientApp extends javax.swing.JFrame {

    private CardLayout cardLayout;
    private JPanel selectedTab;
    private FormUtil toyFormUtil;
    private FormUtil manufacturerFormUtil;
    private JTextField[] toyTextFields;
    private JTextField[] manufacturerTextFields;
    private ClientProtocol clientProtocol;

    private static String[] toyIds;
    private static String[] toyInfo;
    private static String[] manufacturerInfo;
    private int manufacturerId;
    private String manufacturer;

    /**
     * Creates new form ClientApp
     */
    public ClientApp() {
        initComponents();
        modifyComponenets();
        initVariables();
        populateAllComboBoxes();
        initCardLayout();
        setManufacturerNameAndId();
    }

    private void modifyComponenets() {
        //make the JDateChooser editor uneditable
        manuFactureDate.getDateEditor().setEnabled(false);
        manuFactureDate.setDate(new Date());
    }

    private void initVariables() {
        //Textfield arrays
        toyTextFields = new JTextField[]{name, description, price, batchNumber};
        manufacturerTextFields = new JTextField[]{companyName, streetAddress, zipCode};

        //Form Utilities
        toyFormUtil = new FormUtil(toyTextFields);
        manufacturerFormUtil = new FormUtil(manufacturerTextFields);

        setToyDetails();
        setManufacturerDetails();

        //Init the client protocol
        clientProtocol = new ClientProtocol();
    }

    private static void setToyDetails() {
        //Combobox variables 
        toyIds = Toys.selectIdentificationDetails();
        toyInfo = Toys.selectToys();
    }

    private static void setManufacturerDetails() {
        manufacturerInfo = Manufacturers.selectManufacturers();
    }

    private void initCardLayout() {
        cardLayout = (CardLayout) (cardContainer.getLayout());
        selectedTab = newToyTab;
    }

    private void populateAllComboBoxes() {
        populateCountriesComboBox();
        populateManufacturersComboBox();
        populatechooseActionBox();
        populateInfoComboBoxModels(0);
    }

    private void populateCountriesComboBox() {
        for (String country : CountryUtil.countries) {
            countryComboBox.addItem(country);
        }

        ActionListener countriesComboBoxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(countryComboBox.getSelectedIndex() + ": " + countryComboBox.getSelectedItem());
            }
        };

        countryComboBox.addActionListener(countriesComboBoxListener);
    }

    public void populateManufacturersComboBox() {
        String[] manufacturers = Manufacturers.selectIdentificationDetails();

        if (manufacturers.length > 0) {
            for (String man : manufacturers) {
                manufacturerComboBox.addItem(man);
            }
        }

        ActionListener manufacturersComboBoxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setManufacturerNameAndId();
            }
        };

        manufacturerComboBox.addActionListener(manufacturersComboBoxListener);
    }

    private void setManufacturerNameAndId() {
        try {
            //Get the slected item as string without any whitespaces
            String selectedItem = manufacturerComboBox.getSelectedItem().toString().replaceAll("\\s+", "");
            String[] selectItemArray = selectedItem.split("-");
            manufacturerId = Integer.parseInt(selectItemArray[0]);
            manufacturer = selectItemArray[1];
            System.out.printf("Manufacturer Id: %s\n", manufacturerId);
        } catch (NullPointerException e) {
            System.err.println("No manufacturers yet");
        }
    }

    private String getManufacturerId() {
        return Integer.toString(manufacturerId);
    }

    private String getManufacturer() {
        return manufacturer;
    }

    private void populatechooseActionBox() {
        String[] actions = clientProtocol.getActionStrings();

        for (String action : actions) {
            chooseActionComboBox.addItem(action);
        }

        ActionListener chooseActionComboBoxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateInfoComboBoxModels(chooseActionComboBox.getSelectedIndex());
            }
        };

        chooseActionComboBox.addActionListener(chooseActionComboBoxListener);
    }

    private static void populateInfoComboBoxModels(int i) {
        try {
            ComboBoxModel[] models = new ComboBoxModel[6];

            models[ClientProtocol.SEND_TOY_ID] = new DefaultComboBoxModel(toyIds);
            models[ClientProtocol.SEND_TOY_INFO] = new DefaultComboBoxModel(toyInfo);
            models[ClientProtocol.SEND_MANUFACTURER_INFO] = new DefaultComboBoxModel(manufacturerInfo);
            models[ClientProtocol.SEND_ALL_TOYS] = new DefaultComboBoxModel(new String[]{"<All Toy Info>"});
            models[ClientProtocol.SEND_THANK_YOU] = new DefaultComboBoxModel(new String[]{"Thank you"});
            models[ClientProtocol.REQUEST_CONNECTION_STATUS] = new DefaultComboBoxModel(new String[]{"Send Connection Status"});

            infoComboBox.setModel(models[i]);
        } catch (NullPointerException e) {
            System.err.println("No toys to display yet");
        }
    }

//    public static void updateToyComboBoxes(Toy toy) {
//        setToyDetails();
//        populateInfoComboBoxModels(0);
//    }
//
//    public static void updateManufacturerComboBox(Manufacturer man) {
//        
//    }
    public static void updateToyComboBoxes(Toy toy) {
        updateToyIds(toy);
        updateToyInfo(toy);
        populateInfoComboBoxModels(0);
    }

    private static void updateToyIds(Toy toy) {
        //Toy ids
        List<String> toyIdsList;
        toyIdsList = new ArrayList<>(Arrays.asList(toyIds));
        toyIdsList.add(toy.toyIdentificationToString());
        toyIds = toyIdsList.toArray(new String[0]);
    }

    private static void updateToyInfo(Toy toy) {
        //Toy info
        List<String> toyInfoList = new ArrayList<>(Arrays.asList(toyInfo));
        toyInfoList.add(toy.toyInformationToString());
        toyInfo = toyInfoList.toArray(new String[0]);
    }

    public static void updateManufacturerComboBox(Manufacturer man) {
        //Manufacturer info
        List<String> manufacturerInfoList;

        //Fail safe to handle null cases
        if (manufacturerInfo != null) {
            manufacturerInfoList = new ArrayList<>(Arrays.asList(manufacturerInfo));
        } else {
            manufacturerInfoList = new ArrayList<>();
        }

        manufacturerInfoList.add(man.manufacturerInfoToString());
        manufacturerInfo = manufacturerInfoList.toArray(new String[0]);
        manufacturerComboBox.addItem(man.identificationDetailsToString());
    }

    private void switchPanel(JPanel clickedTab) {
        if (selectedTab == clickedTab) {
            return;
        }

        selectedTab.setBackground(new Color(0, 0, 0));
        clickedTab.setBackground(new Color(51, 51, 51));
        selectedTab = clickedTab;
    }

    public static void setReceivedMessage(String msgReceived) {
        String message = String.format("%s\n [SERVER@%s]: %s", messageTextArea.getText(), TimeUtil.getLocalTimeNow(),
                msgReceived);
        messageTextArea.setText(message);
    }

    private String getDateString(Date date) {
        return DateFormat.getDateInstance().format(date);
    }

    public Map<String, String> getNewToyInput() {
        Map<String, String> toyInput = null;

        try {
            toyInput = new HashMap(
                    Map.ofEntries(Map.entry(Toy.NAME, name.getText()),
                            Map.entry(Toy.DESCRIPTION, description.getText()),
                            Map.entry(Toy.PRICE, price.getText()),
                            Map.entry(Toy.MANUFACTURER, getManufacturer()),
                            Map.entry(Toy.MANUFACTURER_ID, getManufacturerId()),
                            Map.entry(Toy.MANUFACTURE_DATE, getDateString(manuFactureDate.getDate())),
                            Map.entry(Toy.BATCH_NUMBER, batchNumber.getText())
                    )
            );
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
            toyFormUtil.errorFeedback(FormUtil.MISSING_FIELDS_TITLE, "Some fields values are missing");
        }

        return toyInput;
    }

    public HashMap<String, String> getNewManufacturerInput() {
        return new HashMap(
                Map.ofEntries(
                        Map.entry(Manufacturer.COMPANY_NAME, companyName.getText()),
                        Map.entry(Manufacturer.STREET_ADDRESS, streetAddress.getText()),
                        Map.entry(Manufacturer.ZIP_CODE, zipCode.getText()),
                        Map.entry(Manufacturer.COUNTRY, countryComboBox.getSelectedItem().toString())
                )
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        sidePanel = new javax.swing.JPanel();
        newToyTab = new javax.swing.JPanel();
        newToyLabel = new javax.swing.JLabel();
        newManufacturerTab = new javax.swing.JPanel();
        newManufacturerLabel = new javax.swing.JLabel();
        sendInfoTab = new javax.swing.JPanel();
        sendInfoLabel = new javax.swing.JLabel();
        sendFeedbackTab = new javax.swing.JPanel();
        sendFeedbackLabel = new javax.swing.JLabel();
        cardContainer = new javax.swing.JPanel();
        newToyPanel = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        description = new javax.swing.JTextField();
        price = new javax.swing.JTextField();
        priceLabel = new javax.swing.JLabel();
        manufactureDateLabel = new javax.swing.JLabel();
        manufacturerLabel = new javax.swing.JLabel();
        submitNewToyBtn = new javax.swing.JButton();
        manuFactureDate = new com.toedter.calendar.JDateChooser();
        batchNumberLabel = new javax.swing.JLabel();
        batchNumber = new javax.swing.JTextField();
        manufacturerComboBox = new javax.swing.JComboBox<>();
        newManufacturerPanel = new javax.swing.JPanel();
        nameLabel1 = new javax.swing.JLabel();
        submitNewManufacturerBtn = new javax.swing.JButton();
        streetAddress = new javax.swing.JTextField();
        companyName = new javax.swing.JTextField();
        zipCode = new javax.swing.JTextField();
        nameLabel2 = new javax.swing.JLabel();
        nameLabel5 = new javax.swing.JLabel();
        nameLabel6 = new javax.swing.JLabel();
        countryComboBox = new javax.swing.JComboBox<>();
        sendInfoPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        sendBtn = new javax.swing.JButton();
        infoComboBox = new javax.swing.JComboBox<>();
        chooseActionComboBox = new javax.swing.JComboBox<>();
        sendFeedbackPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client Side");
        setResizable(false);

        sidePanel.setBackground(new java.awt.Color(0, 0, 0));
        sidePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        newToyTab.setBackground(new java.awt.Color(51, 51, 51));
        newToyTab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        newToyTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newToyTabMouseClicked(evt);
            }
        });

        newToyLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newToyLabel.setForeground(new java.awt.Color(255, 255, 255));
        newToyLabel.setText("New Toy");

        javax.swing.GroupLayout newToyTabLayout = new javax.swing.GroupLayout(newToyTab);
        newToyTab.setLayout(newToyTabLayout);
        newToyTabLayout.setHorizontalGroup(
            newToyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newToyTabLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(newToyLabel)
                .addContainerGap(92, Short.MAX_VALUE))
        );
        newToyTabLayout.setVerticalGroup(
            newToyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newToyTabLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(newToyLabel)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        sidePanel.add(newToyTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 240, 50));

        newManufacturerTab.setBackground(new java.awt.Color(0, 0, 0));
        newManufacturerTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newManufacturerTabMouseClicked(evt);
            }
        });

        newManufacturerLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newManufacturerLabel.setForeground(new java.awt.Color(255, 255, 255));
        newManufacturerLabel.setText("New Manufacturer");

        javax.swing.GroupLayout newManufacturerTabLayout = new javax.swing.GroupLayout(newManufacturerTab);
        newManufacturerTab.setLayout(newManufacturerTabLayout);
        newManufacturerTabLayout.setHorizontalGroup(
            newManufacturerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newManufacturerTabLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(newManufacturerLabel)
                .addContainerGap(56, Short.MAX_VALUE))
        );
        newManufacturerTabLayout.setVerticalGroup(
            newManufacturerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newManufacturerTabLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(newManufacturerLabel)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        sidePanel.add(newManufacturerTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 240, -1));

        sendInfoTab.setBackground(new java.awt.Color(0, 0, 0));
        sendInfoTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendInfoTabMouseClicked(evt);
            }
        });

        sendInfoLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        sendInfoLabel.setForeground(new java.awt.Color(255, 255, 255));
        sendInfoLabel.setText("Send Information");

        javax.swing.GroupLayout sendInfoTabLayout = new javax.swing.GroupLayout(sendInfoTab);
        sendInfoTab.setLayout(sendInfoTabLayout);
        sendInfoTabLayout.setHorizontalGroup(
            sendInfoTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sendInfoTabLayout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addComponent(sendInfoLabel)
                .addGap(58, 58, 58))
        );
        sendInfoTabLayout.setVerticalGroup(
            sendInfoTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sendInfoTabLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(sendInfoLabel)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        sidePanel.add(sendInfoTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 156, 240, 50));

        sendFeedbackTab.setBackground(new java.awt.Color(0, 0, 0));
        sendFeedbackTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendFeedbackTabMouseClicked(evt);
            }
        });

        sendFeedbackLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        sendFeedbackLabel.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout sendFeedbackTabLayout = new javax.swing.GroupLayout(sendFeedbackTab);
        sendFeedbackTab.setLayout(sendFeedbackTabLayout);
        sendFeedbackTabLayout.setHorizontalGroup(
            sendFeedbackTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sendFeedbackTabLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(sendFeedbackLabel)
                .addContainerGap(171, Short.MAX_VALUE))
        );
        sendFeedbackTabLayout.setVerticalGroup(
            sendFeedbackTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sendFeedbackTabLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(sendFeedbackLabel)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        sidePanel.add(sendFeedbackTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 240, 50));

        jSplitPane1.setLeftComponent(sidePanel);

        cardContainer.setLayout(new java.awt.CardLayout());

        newToyPanel.setBackground(new java.awt.Color(255, 0, 51));

        name.setBackground(new java.awt.Color(255, 255, 255));
        name.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        name.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameActionPerformed(evt);
            }
        });

        descriptionLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        descriptionLabel.setForeground(new java.awt.Color(255, 255, 255));
        descriptionLabel.setText("Description");

        nameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameLabel.setForeground(new java.awt.Color(255, 255, 255));
        nameLabel.setText("Name");

        description.setBackground(new java.awt.Color(255, 255, 255));
        description.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        description.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        description.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descriptionActionPerformed(evt);
            }
        });

        price.setBackground(new java.awt.Color(255, 255, 255));
        price.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        price.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        price.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceActionPerformed(evt);
            }
        });

        priceLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        priceLabel.setForeground(new java.awt.Color(255, 255, 255));
        priceLabel.setText("Price");

        manufactureDateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manufactureDateLabel.setForeground(new java.awt.Color(255, 255, 255));
        manufactureDateLabel.setText("Date of Manufacture");

        manufacturerLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        manufacturerLabel.setForeground(new java.awt.Color(255, 255, 255));
        manufacturerLabel.setText("Manufacturer");

        submitNewToyBtn.setBackground(new java.awt.Color(0, 0, 0));
        submitNewToyBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        submitNewToyBtn.setForeground(new java.awt.Color(255, 255, 255));
        submitNewToyBtn.setText("SUBMIT");
        submitNewToyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitNewToyBtnActionPerformed(evt);
            }
        });

        manuFactureDate.setBackground(new java.awt.Color(255, 255, 255));
        manuFactureDate.setForeground(new java.awt.Color(255, 255, 255));
        manuFactureDate.setDateFormatString("yyyy-MM-dd");
        manuFactureDate.setMinSelectableDate(new java.util.Date(-10800000L));

        batchNumberLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        batchNumberLabel.setForeground(new java.awt.Color(255, 255, 255));
        batchNumberLabel.setText("Batch Number");

        batchNumber.setBackground(new java.awt.Color(255, 255, 255));
        batchNumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        batchNumber.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        batchNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchNumberActionPerformed(evt);
            }
        });

        manufacturerComboBox.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout newToyPanelLayout = new javax.swing.GroupLayout(newToyPanel);
        newToyPanel.setLayout(newToyPanelLayout);
        newToyPanelLayout.setHorizontalGroup(
            newToyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newToyPanelLayout.createSequentialGroup()
                .addContainerGap(136, Short.MAX_VALUE)
                .addGroup(newToyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(manuFactureDate, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(manufacturerComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(batchNumber)
                    .addGroup(newToyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(batchNumberLabel)
                        .addGroup(newToyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(submitNewToyBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(price, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addComponent(description, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addComponent(name)
                            .addComponent(manufacturerLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(manufactureDateLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(priceLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descriptionLabel, javax.swing.GroupLayout.Alignment.LEADING))))
                .addGap(124, 124, 124))
        );
        newToyPanelLayout.setVerticalGroup(
            newToyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newToyPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(priceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(manufacturerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manufacturerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(manufactureDateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manuFactureDate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(batchNumberLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(submitNewToyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        cardContainer.add(newToyPanel, "newToyPanel");

        newManufacturerPanel.setBackground(new java.awt.Color(255, 0, 51));

        nameLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameLabel1.setForeground(new java.awt.Color(255, 255, 255));
        nameLabel1.setText("Country");

        submitNewManufacturerBtn.setBackground(new java.awt.Color(0, 0, 0));
        submitNewManufacturerBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        submitNewManufacturerBtn.setForeground(new java.awt.Color(255, 255, 255));
        submitNewManufacturerBtn.setText("SUBMIT");
        submitNewManufacturerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitNewManufacturerBtnActionPerformed(evt);
            }
        });

        streetAddress.setBackground(new java.awt.Color(255, 255, 255));
        streetAddress.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        streetAddress.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        streetAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                streetAddressActionPerformed(evt);
            }
        });

        companyName.setBackground(new java.awt.Color(255, 255, 255));
        companyName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        companyName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        companyName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyNameActionPerformed(evt);
            }
        });

        zipCode.setBackground(new java.awt.Color(255, 255, 255));
        zipCode.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        zipCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        zipCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zipCodeActionPerformed(evt);
            }
        });

        nameLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameLabel2.setForeground(new java.awt.Color(255, 255, 255));
        nameLabel2.setText("Company Name");

        nameLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameLabel5.setForeground(new java.awt.Color(255, 255, 255));
        nameLabel5.setText("Street Address");

        nameLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nameLabel6.setForeground(new java.awt.Color(255, 255, 255));
        nameLabel6.setText("Zip Code");

        javax.swing.GroupLayout newManufacturerPanelLayout = new javax.swing.GroupLayout(newManufacturerPanel);
        newManufacturerPanel.setLayout(newManufacturerPanelLayout);
        newManufacturerPanelLayout.setHorizontalGroup(
            newManufacturerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newManufacturerPanelLayout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addGroup(newManufacturerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newManufacturerPanelLayout.createSequentialGroup()
                        .addComponent(nameLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(newManufacturerPanelLayout.createSequentialGroup()
                        .addGroup(newManufacturerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(newManufacturerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(submitNewManufacturerBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nameLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(companyName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                .addComponent(nameLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(nameLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(zipCode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                .addComponent(streetAddress, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(0, 126, Short.MAX_VALUE))))
        );
        newManufacturerPanelLayout.setVerticalGroup(
            newManufacturerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newManufacturerPanelLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(nameLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(companyName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(nameLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(streetAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(nameLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(zipCode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(nameLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(submitNewManufacturerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137))
        );

        cardContainer.add(newManufacturerPanel, "newManufacturerPanel");

        sendInfoPanel.setBackground(new java.awt.Color(255, 0, 51));

        messageTextArea.setBackground(new java.awt.Color(255, 255, 255));
        messageTextArea.setColumns(20);
        messageTextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        messageTextArea.setForeground(new java.awt.Color(0, 0, 0));
        messageTextArea.setRows(5);
        jScrollPane1.setViewportView(messageTextArea);

        sendBtn.setBackground(new java.awt.Color(0, 0, 0));
        sendBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        sendBtn.setForeground(new java.awt.Color(255, 255, 255));
        sendBtn.setText("SEND");
        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });

        infoComboBox.setEditable(true);

        javax.swing.GroupLayout sendInfoPanelLayout = new javax.swing.GroupLayout(sendInfoPanel);
        sendInfoPanel.setLayout(sendInfoPanelLayout);
        sendInfoPanelLayout.setHorizontalGroup(
            sendInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sendInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sendInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sendInfoPanelLayout.createSequentialGroup()
                        .addComponent(chooseActionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(infoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sendInfoPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167))
        );
        sendInfoPanelLayout.setVerticalGroup(
            sendInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sendInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sendInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(infoComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(chooseActionComboBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        cardContainer.add(sendInfoPanel, "sendInfoPanel");

        sendFeedbackPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout sendFeedbackPanelLayout = new javax.swing.GroupLayout(sendFeedbackPanel);
        sendFeedbackPanel.setLayout(sendFeedbackPanelLayout);
        sendFeedbackPanelLayout.setHorizontalGroup(
            sendFeedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 514, Short.MAX_VALUE)
        );
        sendFeedbackPanelLayout.setVerticalGroup(
            sendFeedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        cardContainer.add(sendFeedbackPanel, "sendFeedbackPanel");

        jSplitPane1.setRightComponent(cardContainer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 759, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newToyTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newToyTabMouseClicked
        // TODO add your handling code here:
        cardLayout.show(cardContainer, "newToyPanel");
        switchPanel(newToyTab);
    }//GEN-LAST:event_newToyTabMouseClicked

    private void newManufacturerTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newManufacturerTabMouseClicked
        // TODO add your handling code here:
        cardLayout.show(cardContainer, "newManufacturerPanel");
        switchPanel(newManufacturerTab);
    }//GEN-LAST:event_newManufacturerTabMouseClicked

    private void priceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceActionPerformed

    private void descriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descriptionActionPerformed

    private void nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameActionPerformed

    private void submitNewToyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitNewToyBtnActionPerformed
        // TODO add your handling code here:
        //Validate form, if validation passes, send to clientProtocol for processing
        Map<String, String> formInput = getNewToyInput();
        Message validationMessage = toyFormUtil.validateForm(formInput);

        if (!validationMessage.getStatus()) {
            toyFormUtil.errorFeedback(FormUtil.MISSING_FIELDS_TITLE, validationMessage.getMessage());

        } else {//If validation passes
            clientProtocol.insertNewToy(Toys.INSERT_SUCCESS_MSG, formInput);
            toyFormUtil.clearTextFields();
        }

    }//GEN-LAST:event_submitNewToyBtnActionPerformed

    private void batchNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batchNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_batchNumberActionPerformed

    private void sendInfoTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendInfoTabMouseClicked
        // TODO add your handling code here:
        cardLayout.show(cardContainer, "sendInfoPanel");
        switchPanel(sendInfoTab);
    }//GEN-LAST:event_sendInfoTabMouseClicked

    private void sendFeedbackTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendFeedbackTabMouseClicked
        // TODO add your handling code here:
//        cardLayout.show(cardContainer, "sendFeedbackPanel");
//        switchPanel(sendFeedbackTab);
    }//GEN-LAST:event_sendFeedbackTabMouseClicked

    private void submitNewManufacturerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitNewManufacturerBtnActionPerformed
        // TODO add your handling code here:        
        Map<String, String> formInput = getNewManufacturerInput();
        Message validationMessage = manufacturerFormUtil.validateForm(formInput);

        if (!validationMessage.getStatus()) {
            manufacturerFormUtil.errorFeedback(FormUtil.MISSING_FIELDS_TITLE, validationMessage.getMessage());

        } else {//If validation passes
            clientProtocol.insertNewManufacturer(Manufacturers.INSERT_SUCCESS_MSG, formInput);
            manufacturerFormUtil.clearTextFields();
        }
    }//GEN-LAST:event_submitNewManufacturerBtnActionPerformed

    private void streetAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_streetAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_streetAddressActionPerformed

    private void companyNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_companyNameActionPerformed

    private void zipCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zipCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zipCodeActionPerformed

    private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBtnActionPerformed
        // TODO add your handling code here:
        int actionSelected = chooseActionComboBox.getSelectedIndex();
        String previousMessageStream = messageTextArea.getText();
        clientProtocol.setPreviousMessageStream(previousMessageStream);
        chooseAction(actionSelected);//Pick what action depending on what item was chosen 
    }//GEN-LAST:event_sendBtnActionPerformed

    private void chooseAction(int actionIndex) {
        String defaultMessage = infoComboBox.getSelectedItem().toString();

        switch (actionIndex) {

            case ClientProtocol.SEND_ALL_TOYS -> {//Custom logic
                clientProtocol.sendMessage(getAllToys());
            }

            case ClientProtocol.SEND_THANK_YOU -> {   //Custom Logic..         
                clientProtocol.sendMessage(getThankYouMessage(defaultMessage));
            }

            case ClientProtocol.REQUEST_CONNECTION_STATUS -> {
                clientProtocol.sendMessage(ClientProtocol.REQUEST_CONNECTION_STRING);
            }

            default ->
                //SEND_TOY_ID
                //SEND_TOY_INFO
                //SEND_MANUFACTURER_INFO
                clientProtocol.sendMessage(defaultMessage);
        }
    }

    private String getAllToys() {
        String allToys = "All toys:\n";
        for (String toy : toyInfo) {
            allToys += String.format("%s\n", toy);
        }
        return allToys;
    }

    private String getThankYouMessage(String message) {
        String uniqueId = RandomUtil.getRandomId();
        return new ThankYouMessage(uniqueId, message).messageToString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientApp().setVisible(true);
            }
        });
        //Open connection to client socket
        SocketClient.openClientSocket();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField batchNumber;
    private javax.swing.JLabel batchNumberLabel;
    private javax.swing.JPanel cardContainer;
    private javax.swing.JComboBox<String> chooseActionComboBox;
    private javax.swing.JTextField companyName;
    private javax.swing.JComboBox<String> countryComboBox;
    private javax.swing.JTextField description;
    private javax.swing.JLabel descriptionLabel;
    private static javax.swing.JComboBox<String> infoComboBox;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private com.toedter.calendar.JDateChooser manuFactureDate;
    private javax.swing.JLabel manufactureDateLabel;
    private static javax.swing.JComboBox<String> manufacturerComboBox;
    private javax.swing.JLabel manufacturerLabel;
    private static javax.swing.JTextArea messageTextArea;
    private javax.swing.JTextField name;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel nameLabel1;
    private javax.swing.JLabel nameLabel2;
    private javax.swing.JLabel nameLabel5;
    private javax.swing.JLabel nameLabel6;
    private javax.swing.JLabel newManufacturerLabel;
    private javax.swing.JPanel newManufacturerPanel;
    private javax.swing.JPanel newManufacturerTab;
    private javax.swing.JLabel newToyLabel;
    private javax.swing.JPanel newToyPanel;
    private javax.swing.JPanel newToyTab;
    private javax.swing.JTextField price;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JButton sendBtn;
    private javax.swing.JLabel sendFeedbackLabel;
    private javax.swing.JPanel sendFeedbackPanel;
    private javax.swing.JPanel sendFeedbackTab;
    private javax.swing.JLabel sendInfoLabel;
    private javax.swing.JPanel sendInfoPanel;
    private javax.swing.JPanel sendInfoTab;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JTextField streetAddress;
    private javax.swing.JButton submitNewManufacturerBtn;
    private javax.swing.JButton submitNewToyBtn;
    private javax.swing.JTextField zipCode;
    // End of variables declaration//GEN-END:variables

}
