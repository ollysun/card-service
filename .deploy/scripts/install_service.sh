#!/bin/bash
cp /opt/vayapay/secure_card_storage/service/cardIdentification.service /etc/systemd/system/
systemctl enable cardIdentification.service
