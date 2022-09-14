#!/bin/bash
cp /opt/vayapay/card_identification/service/cardIdentification.service /etc/systemd/system/
systemctl enable cardIdentification.service
