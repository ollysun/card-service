#!/usr/bin/env bash
# based on https://gitlab.com/gitlab-org/cloud-deploy/-/blob/master/aws/src/bin/gl-ec2
deployment_id=$(echo "$1" | jq -r '.deploymentId')

echo "Awaiting successful deployment of $deployment_id"

deployment_status=''
deployment_description=''

monitor_deployment() {
  sleep 10

  get_deployment_status

  if [ "$deployment_status" != "Failed" ] && [ "$deployment_status" != "Succeeded" ]; then
    monitor_deployment
  fi
}

get_deployment_status() {
  deployment_description=$(aws deploy get-deployment --deployment-id "$deployment_id")
  deployment_status=$(echo "$deployment_description" | jq -r '.deploymentInfo.status')
}

monitor_deployment

if [ "$deployment_status" == "Succeeded" ]; then
  echo "Deployment to EC2 instance has now completed successfully."
else
  echo "Deployment to EC2 instance has failed:"
  echo "$deployment_description"
  exit 1
fi

exit 0
